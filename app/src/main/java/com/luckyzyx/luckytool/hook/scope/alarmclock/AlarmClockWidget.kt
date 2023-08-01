package com.luckyzyx.luckytool.hook.scope.alarmclock

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.ArrayMap
import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.android.RemoteViewsClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import java.util.Arrays

object AlarmClockWidget : YukiBaseHooker() {

    private lateinit var redMode: String

    override fun onHook() {
        redMode = prefs(ModulePrefs).getString("alarmclock_widget_redone_mode", "0")
        dataChannel.wait<String>("alarmclock_widget_redone_mode") { redMode = it }

        val clazz = "com.coloros.widget.smallweather.OnePlusWidget".toClassOrNull() ?: return
        if (clazz.hasMethod { param(StringClass, StringClass);returnType = CharSequenceClass }) {
            loadHooker(AlarmClock12);return
        }
        if (clazz.hasMethod { returnType(RemoteViewsClass) }) {
            loadHooker(AlarmClock13);return
        }
        loadHooker(AlarmClock131)
    }

    private fun setCharRedOne(format: CharSequence): CharSequence {
        val sp = SpannableStringBuilder(format)
        val length = if (format.contains(":")) format.toString().substringBefore(":").length
        else if (format.contains("\u2236")) format.toString().substringBefore("\u2236").length
        else format.length
        for (i in 0 until length) {
            if (format[i].toString() == "1") {
                val colorRes = Color.parseColor("#c41442")
                sp.setSpan(ForegroundColorSpan(colorRes), i, i + 1, 34)
            }
        }
        return sp
    }

    private object AlarmClock12 : YukiBaseHooker() {
        override fun onHook() {
            //Source OnePlusWidget
            findClass("com.coloros.widget.smallweather.OnePlusWidget").hook {
                injectMember {
                    method {
                        param(StringClass, StringClass)
                        returnType = CharSequenceClass
                    }
                    afterHook {
                        if (redMode == "0") return@afterHook
                        result = when (redMode) {
                            "1" -> result<CharSequence>()?.let { setCharRedOne(it) }
                            "2" -> result<CharSequence>().toString()
                            else -> result
                        }
                    }
                }
            }
        }
    }

    private object AlarmClock13 : YukiBaseHooker() {
        override fun onHook() {
            //OnePlusWidget setTextViewText -> local_hour_txt -> SpannableStringBuilder -> CharSequence
            searchClass {
                from("m0", "j0", "o0", "l0", "k0").absolute()
                field { type = CharSequenceClass }.count(1)
                field { type = HandlerClass }.count(1)
                field { type = BooleanType }.count(3)
                method {
                    param(ContextClass, StringClass, StringClass)
                    returnType = CharSequenceClass
                }.count(1)
                method {
                    param(ContextClass, StringClass)
                    returnType = CharSequenceClass
                }.count(1)
            }.get()?.hook {
                injectMember {
                    method {
                        param { it[0] == ContextClass && it[1] == StringClass }
                        paramCount(2..3)
                    }.all()
                    afterHook {
                        if (redMode == "0") return@afterHook
                        result = when (redMode) {
                            "1" -> result<CharSequence>()?.let { setCharRedOne(it) }
                            "2" -> result<CharSequence>().toString()
                            else -> result
                        }
                    }
                }
            }
        }
    }

    private object AlarmClock131 : YukiBaseHooker() {
        override fun onHook() {
            val appSet =
                prefs(ModulePrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
                    Arrays.sort(this)
                    forEach {
                        this[this.indexOf(it)] = it.substring(2)
                    }
                }
            val list = ArrayMap<String, Array<String>>()
            when (appSet[2]) {
                "65b9601", "d29dc32", "546b861" -> list["a5.v"] = arrayOf("u", "t")
            }
            if (list.keys.isEmpty()) {
                loggerD(msg = "该版本未进行适配!")
                loggerD(msg = "This version is not adapted!")
                return
            }

            //Source OppoWeather / OppoWeatherSingle / OppoWeatherVertical
            //Search Class com.oplus.widget.OplusTextClock
            findClass(list.keyAt(0)).hook {
                injectMember {
                    method { emptyParam();returnType = BooleanType }.all()
                    beforeHook {
                        if (list.valueAt(0).contains(method.name)) {
                            when (redMode) {
                                "1" -> resultTrue()
                                "2" -> resultFalse()
                            }
                        }
                    }
                }
            }
        }
    }
}
