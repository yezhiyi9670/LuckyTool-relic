package com.luckyzyx.luckytool.hook.scope.alarmclock

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.highcapable.yukihookapi.hook.bean.HookClass
import com.highcapable.yukihookapi.hook.core.YukiMemberHookCreator
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.android.RemoteViewsClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.safeOfNull

object AlarmClockWidget : YukiBaseHooker() {

    private lateinit var redMode: String

    override fun onHook() {
        redMode = prefs(ModulePrefs).getString("alarmclock_widget_redone_mode", "0")
        dataChannel.wait<String>("alarmclock_widget_redone_mode") { redMode = it }

        val clazz = "com.coloros.widget.smallweather.OnePlusWidget".toClassOrNull() ?: return
        if (clazz.hasMethod { param(StringClass, StringClass) }) {
            loadHooker(AlarmClock12)
        } else if (clazz.hasMethod { returnType(RemoteViewsClass) }) {
            loadHooker(AlarmClock13)
        } else loadHooker(AlarmClock131())
    }

    private class AlarmClock131 : YukiBaseHooker() {
        override fun onHook() {
            //Source OnePlusWidget / OppoWeather / OppoWeatherSingle / OppoWeatherVertical
            //Search Class com.oplus.widget.OplusTextClock
            findClass("com.coloros.widget.smallweather.OnePlusWidget").injHook()
            findClass("com.coloros.widget.smallweather.OppoWeather").injHook()
            findClass("com.coloros.widget.smallweather.OppoWeatherSingle").injHook()
            findClass("com.coloros.widget.smallweather.OppoWeatherVertical").injHook()
            "com.coloros.widget.smallweather.OppoWeatherMultiVertical".toClassOrNull()
                ?.hook { injMember() }
        }

        fun HookClass.injHook() {
            hook { injMember() }
        }

        fun YukiMemberHookCreator.injMember(): YukiMemberHookCreator.MemberHookCreator.Result {
            return injectMember {
                method { emptyParam();returnType = IntType }.all()
                afterHook {
                    if (redMode == "0") return@afterHook
                    val context = field { type = ContextClass;superClass() }.get(instance)
                        .cast<Context>() ?: return@afterHook
                    val resId = result<Int>() ?: return@afterHook
                    if (resId < 1000) return@afterHook
                    val entryName = safeOfNull { context.resources.getResourceEntryName(resId) }
                        ?: return@afterHook
                    result = (getReplaceLayout(context, entryName, redMode) ?: return@afterHook)
                }
            }
        }

        @SuppressLint("DiscouragedApi")
        fun getReplaceLayout(context: Context, layoutName: String, redMode: String): Int? {
            val curRedMode = layoutName.contains("red")
            val entryName = when (redMode) {
                "1" -> if (curRedMode) layoutName else getRedLayoutRes(layoutName)
                "2" -> if (curRedMode) getNonRedLayoutRes(layoutName) else layoutName
                else -> return null
            }
            val resId = context.resources.getIdentifier(entryName, "layout", packageName)
            return resId.takeIf { it != 0 }
        }

        /**
         * 获取非红一布局
         * @param layoutName String?
         * @return String?
         */
        fun getNonRedLayoutRes(layoutName: String?): String? {
            return when (layoutName) {
                //OnePlusWidget
                "op_double_clock_red_widget_land_view" -> "op_double_clock_widget_land_view"
                "op_double_clock_red_widget_view" -> "op_double_clock_widget_view"
                "one_plus_red_widget_land_view" -> "one_plus_widget_land_view"
                "one_plus_red_widget_view" -> "one_plus_widget_view"
                "table_op_double_clock_red_widget_land_view" -> "table_op_double_clock_widget_land_view"
                "table_op_double_clock_red_widget_view" -> "table_op_double_clock_widget_view"
                "table_one_plus_red_widget_land_view" -> "table_one_plus_widget_land_view"
                "table_one_plus_red_widget_view" -> "table_one_plus_widget_view"
                //OppoWeather
                "hor_double_clock_red_widget_land_view_t" -> "hor_double_clock_widget_land_view_t"
                "hor_double_clock_red_widget_view_t" -> "hor_double_clock_widget_view_t"
                "hor_single_clock_red_widget_land_view_t" -> "hor_single_clock_widget_land_view_t"
                "hor_single_clock_red_widget_view_t" -> "hor_single_clock_widget_view_t"
                "table_hor_double_clock_red_widget_land_view_t" -> "table_hor_double_clock_widget_land_view_t"
                "table_hor_double_clock_red_widget_view_t" -> "table_hor_double_clock_widget_view_t"
                "table_hor_single_clock_red_widget_land_view_t" -> "table_hor_single_clock_widget_land_view_t"
                "table_hor_single_clock_red_widget_view_t" -> "table_hor_single_clock_widget_view_t"
                //OppoWeatherSingle
                "one_line_double_clock_red_widget_land_view_t" -> "one_line_double_clock_widget_land_view_t"
                "one_line_double_clock_red_widget_view_t" -> "one_line_double_clock_widget_view_t"
                "one_line_hor_single_clock_red_widget_land_view_t" -> "one_line_hor_single_clock_widget_land_view_t"
                "one_line_hor_single_clock_red_widget_view_t" -> "one_line_hor_single_clock_widget_view_t"
                "table_one_line_double_clock_red_widget_land_view_t" -> "table_one_line_double_clock_widget_land_view_t"
                "table_one_line_double_clock_red_widget_view_t" -> "table_one_line_double_clock_widget_view_t"
                "table_one_line_hor_single_clock_red_widget_land_view_t" -> "table_one_line_hor_single_clock_widget_land_view_t"
                "table_one_line_hor_single_clock_red_widget_view_t" -> "table_one_line_hor_single_clock_widget_view_t"
                //OppoWeatherVertical
                "vertical_double_clock_red_widget_land_view_t" -> "vertical_double_clock_widget_land_view_t"
                "vertical_double_clock_red_widget_view_t" -> "vertical_double_clock_widget_view_t"
                "vertical_single_clock_red_widget_land_view_t" -> "vertical_single_clock_widget_land_view_t"
                "vertical_single_clock_red_widget_view_t" -> "vertical_single_clock_widget_view_t"
                "table_vertical_double_clock_red_widget_land_view_t" -> "table_vertical_double_clock_widget_land_view_t"
                "table_vertical_double_clock_red_widget_view_t" -> "table_vertical_double_clock_widget_view_t"
                "table_vertical_single_clock_red_widget_land_view_t" -> "table_vertical_single_clock_widget_land_view_t"
                //"vertical_single_clock_red_widget_view_t" -> "vertical_single_clock_widget_view_t"
                //OppoWeatherMultiVertical
                //"hor_double_clock_red_widget_land_view_t" -> "hor_double_clock_widget_land_view_t"
                //"hor_double_clock_red_widget_view_t" -> "hor_double_clock_widget_view_t"
                //"hor_single_clock_red_widget_land_view_t" -> "hor_single_clock_widget_land_view_t"
                "vertical_multi_clock_red_widget_view_t" -> "vertical_multi_clock_widget_view_t"
                //"table_hor_double_clock_red_widget_land_view_t" -> "table_hor_double_clock_widget_land_view_t"
                //"table_hor_double_clock_red_widget_view_t" -> "table_hor_double_clock_widget_view_t"
                //"table_hor_single_clock_red_widget_land_view_t" -> "table_hor_single_clock_widget_land_view_t"
                "table_vertical_multi_clock_red_widget_view_t" -> "table_vertical_multi_clock_widget_view_t"
                else -> null
            }
        }

        /**
         * 获取红一布局
         * @param layoutName String?
         * @return String?
         */
        fun getRedLayoutRes(layoutName: String?): String? {
            return when (layoutName) {
                //OnePlusWidget
                "op_double_clock_widget_land_view" -> "op_double_clock_red_widget_land_view"
                "op_double_clock_widget_view" -> "op_double_clock_red_widget_view"
                "one_plus_widget_land_view" -> "one_plus_red_widget_land_view"
                "one_plus_widget_view" -> "one_plus_red_widget_view"
                "table_op_double_clock_widget_land_view" -> "table_op_double_clock_red_widget_land_view"
                "table_op_double_clock_widget_view" -> "table_op_double_clock_red_widget_view"
                "table_one_plus_widget_land_view" -> "table_one_plus_red_widget_land_view"
                "table_one_plus_widget_view" -> "table_one_plus_red_widget_view"
                //OppoWeather
                "hor_double_clock_widget_land_view_t" -> "hor_double_clock_red_widget_land_view_t"
                "hor_double_clock_widget_view_t" -> "hor_double_clock_red_widget_view_t"
                "hor_single_clock_widget_land_view_t" -> "hor_single_clock_red_widget_land_view_t"
                "hor_single_clock_widget_view_t" -> "hor_single_clock_red_widget_view_t"
                "table_hor_double_clock_widget_land_view_t" -> "table_hor_double_clock_red_widget_land_view_t"
                "table_hor_double_clock_widget_view_t" -> "table_hor_double_clock_red_widget_view_t"
                "table_hor_single_clock_widget_land_view_t" -> "table_hor_single_clock_red_widget_land_view_t"
                "table_hor_single_clock_widget_view_t" -> "table_hor_single_clock_red_widget_view_t"
                //OppoWeatherSingle
                "one_line_double_clock_widget_land_view_t" -> "one_line_double_clock_red_widget_land_view_t"
                "one_line_double_clock_widget_view_t" -> "one_line_double_clock_red_widget_view_t"
                "one_line_hor_single_clock_widget_land_view_t" -> "one_line_hor_single_clock_red_widget_land_view_t"
                "one_line_hor_single_clock_widget_view_t" -> "one_line_hor_single_clock_red_widget_view_t"
                "table_one_line_double_clock_widget_land_view_t" -> "table_one_line_double_clock_red_widget_land_view_t"
                "table_one_line_double_clock_widget_view_t" -> "table_one_line_double_clock_red_widget_view_t"
                "table_one_line_hor_single_clock_widget_land_view_t" -> "table_one_line_hor_single_clock_red_widget_land_view_t"
                "table_one_line_hor_single_clock_widget_view_t" -> "table_one_line_hor_single_clock_red_widget_view_t"
                //OppoWeatherVertical
                "vertical_double_clock_widget_land_view_t" -> "vertical_double_clock_red_widget_land_view_t"
                "vertical_double_clock_widget_view_t" -> "vertical_double_clock_red_widget_view_t"
                "vertical_single_clock_widget_land_view_t" -> "vertical_single_clock_red_widget_land_view_t"
                "vertical_single_clock_widget_view_t" -> "vertical_single_clock_red_widget_view_t"
                "table_vertical_double_clock_widget_land_view_t" -> "table_vertical_double_clock_red_widget_land_view_t"
                "table_vertical_double_clock_widget_view_t" -> "table_vertical_double_clock_red_widget_view_t"
                "table_vertical_single_clock_widget_land_view_t" -> "table_vertical_single_clock_red_widget_land_view_t"
                //"vertical_single_clock_red_widget_view_t" -> "vertical_single_clock_widget_view_t"
                //OppoWeatherMultiVertical
                //"hor_double_clock_red_widget_land_view_t" -> "hor_double_clock_widget_land_view_t"
                //"hor_double_clock_red_widget_view_t" -> "hor_double_clock_widget_view_t"
                //"hor_single_clock_red_widget_land_view_t" -> "hor_single_clock_widget_land_view_t"
                "vertical_multi_clock_widget_view_t" -> "vertical_multi_clock_red_widget_view_t"
                //"table_hor_double_clock_red_widget_land_view_t" -> "table_hor_double_clock_widget_land_view_t"
                //"table_hor_double_clock_red_widget_view_t" -> "table_hor_double_clock_widget_view_t"
                //"table_hor_single_clock_red_widget_land_view_t" -> "table_hor_single_clock_widget_land_view_t"
                "table_vertical_multi_clock_widget_view_t" -> "table_vertical_multi_clock_red_widget_view_t"
                else -> null
            }
        }

    }

    private object AlarmClock13 : YukiBaseHooker() {
        override fun onHook() {
            //OnePlusWidget setTextViewText -> local_hour_txt -> SpannableStringBuilder -> CharSequence
            DexkitUtils.searchDexClass("AlarmClock13", appInfo.sourceDir) { dexKitBridge ->
                dexKitBridge.findClass {
                    matcher {
                        fields {
                            addForType(BooleanType.name)
                            addForType(HandlerClass.name)
                        }
                        methods {
                            add { returnType(BooleanType.name) }
                            add { returnType(HandlerClass.name) }
                            add { paramTypes(ContextClass.name) }
                            add { paramTypes(ContextClass.name, StringClass.name) }
                            add {
                                paramTypes(
                                    ContextClass.name, StringClass.name, StringClass.name
                                )
                            }
                        }
                    }
                }
            }?.firstOrNull()?.className?.hook {
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
}
