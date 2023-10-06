package com.luckyzyx.luckytool.hook.statusbar

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.luckyzyx.luckytool.hook.utils.sysui.LunarHelperUtils
import com.luckyzyx.luckytool.utils.A11
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.formatDate
import com.luckyzyx.luckytool.utils.is24
import java.lang.reflect.Method
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask

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
    private var customFontsize = prefs(ModulePrefs).getInt("statusbar_clock_custom_fontsize", 0)

    private val userTypeface = prefs(ModulePrefs).getBoolean("statusbar_clock_user_typeface", false)

    private var nowLunar: String? = null
    private var nowTime: Date? = null
    private var newline = ""

    override fun onHook() {
        if (clockMode.isBlank() || clockMode == "0") return
        dataChannel.wait<String>("statusbar_clock_text_alignment") { clockAlignment = it }
        dataChannel.wait<String>("statusbar_clock_custom_format") { customFormat = it }
        dataChannel.wait<Int>("statusbar_clock_custom_fontsize") { customFontsize = it }
        dataChannel.wait<Int>("statusbar_clock_singlerow_fontsize") { singleRowFontSize = it }
        dataChannel.wait<Int>("statusbar_clock_doublerow_fontsize") { doubleRowFontSize = it }
        var context: Context? = null

        //Source Clock
        "com.android.systemui.statusbar.policy.Clock".toClass().apply {
            constructor { paramCount = 3 }.hook {
                after {
                    context = args(0).cast<Context>()
                    val clockView = instance<TextView>()
                    if (clockView.resources.getResourceEntryName(clockView.id) != "clock") return@after
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
            method { name = "getSmallTime";returnType = CharSequenceClass }.hook {
                after {
                    instance<TextView>().apply {
                        if (resources.getResourceEntryName(id) != "clock") return@after
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
        }

        //Source StatClock
        VariousClass(
            "com.oplusos.systemui.statusbar.widget.StatClock", //C12 C13
            "com.oplus.systemui.statusbar.widget.StatClock" //C14
        ).toClass().apply {
            method {
                if (SDK == A11) name = "onConfigChanged"
                if (SDK > A11) name = "onConfigurationChanged"
            }.hook { intercept() }

        }
    }

    private fun initLunar(context: Context) {
        val instance = LunarHelperUtils(appClassLoader).buildInstance(context)
        nowLunar =
            LunarHelperUtils(appClassLoader).getDateToString(instance, System.currentTimeMillis())
    }

    private fun TextView.initView() {
        if (userTypeface) typeface = Typeface.DEFAULT_BOLD
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
            val formatList =
                customFormat.takeIf { e -> e.isNotBlank() && e.contains("\n") }?.split("\n")
                    ?.toMutableList() ?: mutableListOf("0")
            formatList.removeIf { it.isBlank() }
            val rows = formatList.size
            isSingleLine = rows == 1
            setTextSize(
                TypedValue.COMPLEX_UNIT_DIP,
                if (customFontsize != 0) customFontsize.toFloat() else defaultSize
            )
            if (rows != 1) setLineSpacing(0F, 0.8F)
        }
        gravity = if (isSingleLine) Gravity.CENTER else when (clockAlignment) {
            "left" -> Gravity.START or Gravity.CENTER
            "center" -> Gravity.CENTER
            "right" -> Gravity.END or Gravity.CENTER
            else -> Gravity.CENTER
        }
    }

    private fun getFormat(format: String, nowTime: Date, nowLunar: String?): String {
        var finalFormat: String = format
        if (finalFormat.contains("NNNN")) finalFormat = finalFormat.replace("NNNN", nowLunar!!)
        if (finalFormat.contains("NNN")) finalFormat = finalFormat.replace(
            "NNN", nowLunar!!.substring(2, nowLunar.length)
        )
        if (finalFormat.contains("NN")) finalFormat = finalFormat.replace(
            "NN", nowLunar!!.substring(4, nowLunar.length)
        )
        if (finalFormat.contains("N")) {
            val startInt = if (nowLunar!!.length > 8) 7 else 6
            finalFormat = finalFormat.replace("N", nowLunar.substring(startInt, nowLunar.length))
        }
        if (finalFormat.contains("dddd")) finalFormat = finalFormat.replace("dddd", "dd号")
        if (finalFormat.contains("ddd")) finalFormat = finalFormat.replace("ddd", "d号")
        if (finalFormat.contains("FF")) finalFormat = finalFormat.replace("FF", getPeriod(nowTime))
        if (finalFormat.contains("GG")) finalFormat =
            finalFormat.replace("GG", getDoubleHour(nowTime))
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
        var period: String
        var doubleHour: String
        var timeFormat = ""
        timeFormat += if (context.is24) "HH:mm" else "hh:mm"
        if (isSecond) timeFormat += ":ss"
        timeFormat = formatDate(timeFormat, nowTime!!)
        if (isPeriod) {
            if (isZh(context)) {
                period = getPeriod(nowTime!!)
                if (!isHideSpace) period += " "
                timeFormat = period + timeFormat
            } else {
                period = " " + formatDate("a", nowTime!!)
                timeFormat += period
            }
        }
        if (isDoubleHour) {
            doubleHour = getDoubleHour(nowTime!!)
            if (!isHideSpace) doubleHour = "$doubleHour "
            timeFormat = doubleHour + timeFormat
        }
        return timeFormat
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locales[0]
        val language = locale.language
        return language.endsWith("zh")
    }
}