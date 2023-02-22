package com.luckyzyx.luckytool.hook.statusbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.TypefaceClass
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.luckyzyx.luckytool.utils.data.*
import com.luckyzyx.luckytool.utils.tools.ModulePrefs
import java.lang.reflect.Method
import java.util.*

object StatusBarClock : YukiBaseHooker() {

    private val clockMode = prefs(ModulePrefs).getString("statusbar_clock_mode", "0")
    private val isYear = prefs(ModulePrefs).getBoolean("statusbar_clock_show_year", false)
    private val isMonth = prefs(ModulePrefs).getBoolean("statusbar_clock_show_month", false)
    private val isDay = prefs(ModulePrefs).getBoolean("statusbar_clock_show_day", false)
    private val isWeek = prefs(ModulePrefs).getBoolean("statusbar_clock_show_week", false)
    private val isPeriod = prefs(ModulePrefs).getBoolean("statusbar_clock_show_period", false)
    private val isDoubleHour =
        prefs(ModulePrefs).getBoolean("statusbar_clock_show_double_hour", false)
    private val isSecond = prefs(ModulePrefs).getBoolean("statusbar_clock_show_second", false)
    private val isHideSpace = prefs(ModulePrefs).getBoolean("statusbar_clock_hide_spaces", false)
    private val isDoubleRow = prefs(ModulePrefs).getBoolean("statusbar_clock_show_doublerow", false)

    private var clockAlignment =
        prefs(ModulePrefs).getString("statusbar_clock_text_alignment", "center")

    private var singleRowFontSize =
        prefs(ModulePrefs).getInt("statusbar_clock_singlerow_fontsize", 0)
    private var doubleRowFontSize =
        prefs(ModulePrefs).getInt("statusbar_clock_doublerow_fontsize", 0)

    private var customFormat =
        prefs(ModulePrefs).getString("statusbar_clock_custom_format", "HH:mm:ss")
    private var customFontsize =
        prefs(ModulePrefs).getInt("statusbar_clock_custom_fontsize", 0)

    private var nowLunar: String? = null
    private var nowTime: Date? = null
    private var newline = ""

    override fun onHook() {
        if (clockMode.isNotBlank() && clockMode == "0") return

        dataChannel.wait<String>("statusbar_clock_text_alignment") { clockAlignment = it }
        dataChannel.wait<String>("statusbar_clock_custom_format") { customFormat = it }
        dataChannel.wait<Int>("statusbar_clock_custom_fontsize") { customFontsize = it }
        dataChannel.wait<Int>("statusbar_clock_singlerow_fontsize") { singleRowFontSize = it }
        dataChannel.wait<Int>("statusbar_clock_doublerow_fontsize") { doubleRowFontSize = it }

        var context: Context? = null
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                constructor {
                    paramCount = 3
                }
                afterHook {
                    context = args(0).cast<Context>()
                    val clockView = instance<TextView>()
                    clockView.apply {
                        if (resources.getResourceEntryName(id) != "clock") return@afterHook
                    }
                    val d: Method = clockView.javaClass.superclass.getDeclaredMethod("updateClock")
                    val r = Runnable {
                        d.isAccessible = true
                        d.invoke(clockView)
                    }

                    class T : TimerTask() {
                        override fun run() {
                            Handler(clockView.context.mainLooper).post(r)
                        }
                    }
                    Timer().scheduleAtFixedRate(T(), 1000 - System.currentTimeMillis() % 1000, 1000)
                }
            }
            injectMember {
                method {
                    name = "getSmallTime"
                    returnType = CharSequenceClass
                }
                afterHook {
                    instance<TextView>().apply {
                        if (resources.getResourceEntryName(id) != "clock") return@afterHook
                        initView()
                    }
                    val mCalendar = field { name = "mCalendar" }.get(instance).cast<Calendar>()
                    nowTime = mCalendar?.time
                    if (clockMode == "1") result = getDate(context!!) + newline + getTime(context!!)
                    else if (clockMode == "2") {
                        val lunarInstance =
                            "com.oplusos.systemui.keyguard.clock.LunarHelper".toClass()
                                .constructor {
                                    param(ContextClass)
                                }.get().call(context)
                        lunarInstance?.current {
                            val lunar = method {
                                name = "getDateToString"
                            }.invoke<String>(System.currentTimeMillis())
                            nowLunar = lunar
                        }
                        val customFormatFinal = if (customFormat.contains("NNNN")) {
                            customFormat.replace("NNNN", nowLunar!!)
                        } else if (customFormat.contains("NNN")) {
                            customFormat.replace(
                                "NNN",
                                nowLunar!!.substring(2, nowLunar!!.length)
                            )
                        } else if (customFormat.contains("NN")) {
                            customFormat.replace(
                                "NN",
                                nowLunar!!.substring(4, nowLunar!!.length)
                            )
                        } else {
                            customFormat.replace(
                                "N",
                                nowLunar!!.substring(6, nowLunar!!.length)
                            )
                        }
                        result = formatDate(customFormatFinal, nowTime!!)
                    }
                }
            }
            if (SDK == A11 && getColorOSVersion == "V12") {
                injectMember {
                    method {
                        name = "updateShowSeconds"
                    }
                    beforeHook {
                        if (isSecond) field {
                            name = "mShowSeconds"
                        }.get(instance).setTrue()
                    }
                }
            }
        }

        findClass("com.oplusos.systemui.statusbar.widget.StatClock").hook {
            injectMember {
                method {
                    if (SDK == A11) name = "onConfigChanged"
                    if (SDK > A11) name = "onConfigurationChanged"
                }
                replaceUnit {
                    instance<TextView>().apply {
                        typeface = field {
                            name = "defaultFont"
                            type = TypefaceClass
                        }.get(instance).cast<Typeface>()
                    }
                }
            }
        }
    }

    private fun TextView.initView() {
        gravity = when (clockAlignment) {
            "left" -> Gravity.START
            "center" -> Gravity.CENTER
            "right" -> Gravity.END
            else -> Gravity.CENTER
        }
        if (clockMode == "1") {
            isSingleLine = isDoubleRow
            if (isDoubleRow) {
                newline = "\n"
                var defaultSize = 8F
                if (doubleRowFontSize != 0) defaultSize = doubleRowFontSize.toFloat()
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, defaultSize)
                setLineSpacing(0F, 0.8F)
            } else {
                if (singleRowFontSize != 0) {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_DIP,
                        singleRowFontSize.toFloat()
                    )
                }
            }
        } else if (clockMode == "2") {
            val rows = customFormat.takeIf { e -> e.isNotBlank() }?.split("\n")?.size ?: 1
            isSingleLine = rows == 1
            var defaultSize = 8F
            if (customFontsize != 0) defaultSize = customFontsize.toFloat()
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, defaultSize)
            if (rows != 1) setLineSpacing(0F, 0.8F)
        }
    }

    private fun getDate(context: Context): String {
        var dateFormat = ""
        if (isZh(context)) {
            if (isYear) dateFormat += "YY年"
            if (isMonth) dateFormat += "M月"
            if (isDay) dateFormat += "d日"
            if (isWeek) dateFormat += "E"
            if (!isHideSpace && !isDoubleRow) dateFormat += " "
        } else {
            if (isWeek) dateFormat += "E"
            if (!isHideSpace && !isDoubleRow) dateFormat += " "
            if (isMonth) {
                dateFormat += "M"
                if (isDay || isYear) dateFormat += "/"
            }
            if (isDay) {
                dateFormat += "d"
                if (isYear) dateFormat += "/"
            }
            if (isYear) {
                dateFormat += "YY"
            }
            if (!isHideSpace && !isDoubleRow) dateFormat += " "
        }
        return formatDate(dateFormat, nowTime!!)
    }

    private fun getTime(context: Context): String {
        var timeFormat = ""
        timeFormat += if (is24(context)) "HH:mm" else "hh:mm"
        if (isSecond) timeFormat += ":ss"
        timeFormat = formatDate(timeFormat, nowTime!!)
        if (isZh(context)) timeFormat =
            getPeriod(context) + timeFormat else timeFormat += getPeriod(context)
        timeFormat = getDoubleHour() + timeFormat
        return timeFormat
    }

    private fun getPeriod(context: Context): String {
        var period = ""
        if (isPeriod) {
            if (isZh(context)) {
                when (formatDate("HH", nowTime!!)) {
                    "00", "01", "02", "03", "04", "05" -> {
                        period = "凌晨"
                    }
                    "06", "07", "08", "09", "10", "11" -> {
                        period = "上午"
                    }
                    "12" -> {
                        period = "中午"
                    }
                    "13", "14", "15", "16", "17" -> {
                        period = "下午"
                    }
                    "18" -> {
                        period = "傍晚"
                    }
                    "19", "20", "21", "22", "23" -> {
                        period = "晚上"
                    }
                }
                if (!isHideSpace) period += " "
            } else {
                period = " " + formatDate("a", nowTime!!)
            }
        }
        return period
    }

    @SuppressLint("SimpleDateFormat")
    fun getDoubleHour(): String {
        var doubleHour = ""
        if (isDoubleHour) {
            when (formatDate("HH", nowTime!!)) {
                "23", "00" -> {
                    doubleHour = "子时"
                }
                "01", "02" -> {
                    doubleHour = "丑时"
                }
                "03", "04" -> {
                    doubleHour = "寅时"
                }
                "05", "06" -> {
                    doubleHour = "卯时"
                }
                "07", "08" -> {
                    doubleHour = "辰时"
                }
                "09", "10" -> {
                    doubleHour = "巳时"
                }
                "11", "12" -> {
                    doubleHour = "午时"
                }
                "13", "14" -> {
                    doubleHour = "未时"
                }
                "15", "16" -> {
                    doubleHour = "申时"
                }
                "17", "18" -> {
                    doubleHour = "酉时"
                }
                "19", "20" -> {
                    doubleHour = "戌时"
                }
                "21", "22" -> {
                    doubleHour = "亥时"
                }
            }
            if (!isHideSpace) doubleHour = "$doubleHour "
        }
        return doubleHour
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locales[0]
        val language = locale.language
        return language.endsWith("zh")
    }

    private fun is24(context: Context): Boolean {
        val t = Settings.System.getString(context.contentResolver, Settings.System.TIME_12_24)
        return t == "24"
    }
}