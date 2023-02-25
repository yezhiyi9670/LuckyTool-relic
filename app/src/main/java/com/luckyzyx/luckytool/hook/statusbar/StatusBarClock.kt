package com.luckyzyx.luckytool.hook.statusbar

import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.luckyzyx.luckytool.utils.data.A11
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.formatDate
import com.luckyzyx.luckytool.utils.data.getColorOSVersion
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
                        initLunar(context!!)
                        result = formatDate(getFormat(customFormat, nowTime!!, nowLunar), nowTime!!)
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
                intercept()
//                replaceUnit {
//                    instance<TextView>().apply {
//                        typeface = field {
//                            name = "defaultFont"
//                            type = TypefaceClass
//                        }.get(instance).cast<Typeface>()
//                    }
//                }
            }
        }
    }

    private fun initLunar(context: Context) {
        val lunarInstance =
            "com.oplusos.systemui.keyguard.clock.LunarHelper".toClass()
                .constructor {
                    param(ContextClass)
                }.get().call(context)
        lunarInstance?.current {
            nowLunar = method {
                name = "getDateToString"
            }.invoke<String>(System.currentTimeMillis())
        }
    }

    private fun TextView.initView() {
        gravity = when (clockAlignment) {
            "left" -> Gravity.START
            "center" -> Gravity.CENTER
            "right" -> Gravity.END
            else -> Gravity.CENTER
        }
        val defaultSize = 12F
        if (clockMode == "1") {
            isSingleLine = !isDoubleRow
            if (isDoubleRow) {
                newline = "\n"
                setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP,
                    if (doubleRowFontSize != 0) doubleRowFontSize.toFloat() else defaultSize
                )
                setLineSpacing(0F, 0.8F)
            } else {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP,
                    if (singleRowFontSize != 0) singleRowFontSize.toFloat() else defaultSize
                )
            }
        } else if (clockMode == "2") {
            val rows = customFormat.takeIf { e -> e.isNotBlank() }?.split("\n")?.size ?: 1
            isSingleLine = rows == 1
            setTextSize(
                TypedValue.COMPLEX_UNIT_DIP,
                if (customFontsize != 0) customFontsize.toFloat() else defaultSize
            )
            if (rows != 1) setLineSpacing(0F, 0.8F)
        }
    }

    private fun getFormat(format: String, nowTime: Date, nowLunar: String?): String {
        var finalFormat: String
        finalFormat = if (format.contains("NNNN")) {
            format.replace("NNNN", nowLunar!!)
        } else if (format.contains("NNN")) {
            format.replace(
                "NNN",
                nowLunar!!.substring(2, nowLunar.length)
            )
        } else if (format.contains("NN")) {
            format.replace(
                "NN",
                nowLunar!!.substring(4, nowLunar.length)
            )
        } else {
            format.replace(
                "N",
                nowLunar!!.substring(6, nowLunar.length)
            )
        }
        if (finalFormat.contains("FF")) finalFormat = finalFormat.replace("FF", getPeriod(nowTime))
        if (finalFormat.contains("GG")) finalFormat = finalFormat.replace("GG", getDoubleHour(nowTime))
        return finalFormat
    }

    private fun getPeriod(nowTime: Date): String {
        return when (formatDate("HH", nowTime)) {
            "00", "01", "02", "03", "04", "05" -> {
                "凌晨"
            }
            "06", "07", "08", "09", "10", "11" -> {
                "上午"
            }
            "12" -> {
                "中午"
            }
            "13", "14", "15", "16", "17" -> {
                "下午"
            }
            "18" -> {
                "傍晚"
            }
            "19", "20", "21", "22", "23" -> {
                "晚上"
            }
            else -> ""
        }
    }

    private fun getDoubleHour(nowTime: Date): String {
        return when (formatDate("HH", nowTime)) {
            "23", "00" -> {
                "子时"
            }
            "01", "02" -> {
                "丑时"
            }
            "03", "04" -> {
                "寅时"
            }
            "05", "06" -> {
                "卯时"
            }
            "07", "08" -> {
                "辰时"
            }
            "09", "10" -> {
                "巳时"
            }
            "11", "12" -> {
                "午时"
            }
            "13", "14" -> {
                "未时"
            }
            "15", "16" -> {
                "申时"
            }
            "17", "18" -> {
                "酉时"
            }
            "19", "20" -> {
                "戌时"
            }
            "21", "22" -> {
                "亥时"
            }
            else -> ""
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
        var period = ""
        var doubleHour = ""
        var timeFormat = ""
        timeFormat += if (is24(context)) "HH:mm" else "hh:mm"
        if (isSecond) timeFormat += ":ss"
        timeFormat = formatDate(timeFormat, nowTime!!)
        if (isZh(context)) {
            if (isPeriod) {
                period = getPeriod(nowTime!!)
                if (!isHideSpace) period += " "
            }
            timeFormat = period + timeFormat
        } else {
            period = " " + formatDate("a", nowTime!!)
            timeFormat += period
        }
        if (isDoubleHour) {
            doubleHour = getDoubleHour(nowTime!!)
            if (!isHideSpace) doubleHour = "$doubleHour "
        }
        timeFormat = doubleHour + timeFormat
        return timeFormat
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