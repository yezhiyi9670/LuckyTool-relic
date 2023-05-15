package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.luckytool.hook.utils.ThemeColorUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import java.util.Calendar

object LockScreenClock : YukiBaseHooker() {
    override fun onHook() {
        var redMode = prefs(ModulePrefs).getString("lock_screen_clock_redone_mode", "0")
        dataChannel.wait<String>("lock_screen_clock_redone_mode") { redMode = it }

        //OnePlus kgd_red_horizontal_single_clock / kgd_red_horizontal_dual_clock
        //Source RedTextClock
        findClass("com.oplusos.systemui.keyguard.clock.RedTextClock").hook {
            injectMember {
                method { name = "onTimeChanged" }
                afterHook {
                    if (redMode == "0") return@afterHook
                    val mShouldRunTicker =
                        field { name = "mShouldRunTicker" }.get(instance).boolean()
                    if (!mShouldRunTicker) return@afterHook
                    val format = field { name = "format" }.get(instance).string()
                    val mTime =
                        field { name = "mTime" }.get(instance).cast<Calendar>() ?: return@afterHook
                    val mTimeHour = instance<TextView>()
                    val mHour = android.text.format.DateFormat.format(format, mTime) as String
                    mTimeHour.setClockRed(mHour, redMode)
                }
            }
        }
        val timeInfoClazz =
            "com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper\$TimeInfo".toClass()
        //Source RedHorizontalDualClockView kgd_red_horizontal_dual_clock
        findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView").hook {
            injectMember {
                method {
                    param { it[2] == timeInfoClazz }
                    paramCount = 3
                }.all()
                afterHook {
                    loggerD(msg = method.name)
                    val type: String = method.name.let {
                        if (it.contains("updateLocateTime")) "LocateTime"
                        else if (it.contains("updateResidentTime")) "ResidentTime"
                        else return@afterHook
                    }
                    val view = args().first().any() ?: return@afterHook
                    when (type) {
                        "LocateTime" -> {
                            val mLocatedTimeHour =
                                view.current().field { name = "mTvHorizontalLocateClockHour" }
                                    .cast<TextView>() ?: return@afterHook
                            val mLocatedTimeInfo = args().last().any() ?: return@afterHook
                            val mHour = mLocatedTimeInfo.current().method { name = "getHour" }
                                .invoke<String>() ?: return@afterHook
                            mLocatedTimeHour.setClockRed(mHour, redMode)
                        }

                        "ResidentTime" -> {
                            val mResidentTimeHour =
                                view.current().field { name = "mTvHorizontalResidentClockHour" }
                                    .cast<TextView>() ?: return@afterHook
                            val mResidentTimeInfo = args().last().any() ?: return@afterHook
                            val mHour = mResidentTimeInfo.current().method { name = "getHour" }
                                .invoke<String>() ?: return@afterHook
                            mResidentTimeHour.setClockRed(mHour, redMode)
                        }
                    }
                }
            }
        }
        //OPPO/Realme kgd_single_clock / kgd_dual_clock
        //Source SingleClockView kgd_single_clock
        findClass("com.oplusos.systemui.keyguard.clock.SingleClockView").hook {
            injectMember {
                method { name = "updateStandardTime" }
                afterHook {
                    if (redMode == "0") return@afterHook
                    val mTimeHour = field { name = "mTimeHour" }.get(instance).cast<TextView>()
                        ?: return@afterHook
                    val mHour = field { name = "mHour" }.get(instance).string()
                        .takeIf { e -> e.isNotBlank() } ?: return@afterHook
                    mTimeHour.setClockRed(mHour, redMode)
                }
            }
        }
        val weatherInfoClazz =
            "com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper\$WeatherInfo".toClass()
        //Source DualClockView kgd_dual_clock
        findClass("com.oplusos.systemui.keyguard.clock.DualClockView").hook {
            injectMember {
                method { param(weatherInfoClazz) }.all()
                afterHook {
                    val type: String = method.name.let {
                        if (it.contains("updateLocatedTime")) "LocatedTime"
                        else if (it.contains("updateResidentTime")) "ResidentTime"
                        else return@afterHook
                    }
                    when (type) {
                        "LocatedTime" -> {
                            val mLocatedTimeHour =
                                field { name = "mLocatedTimeHour" }.get(instance).cast<TextView>()
                                    ?: return@afterHook
                            val mLocatedTimeInfo =
                                field { name = "mLocatedTimeInfo" }.get(instance).any()
                                    ?: return@afterHook
                            val mHour = mLocatedTimeInfo.current().method { name = "getHour" }
                                .invoke<String>() ?: return@afterHook
                            mLocatedTimeHour.setClockRed(mHour, redMode)
                        }

                        "ResidentTime" -> {
                            val mResidentTimeHour =
                                field { name = "mResidentTimeHour" }.get(instance).cast<TextView>()
                                    ?: return@afterHook
                            val mResidentTimeInfo =
                                field { name = "mResidentTimeInfo" }.get(instance).any()
                                    ?: return@afterHook
                            val mHour = mResidentTimeInfo.current().method { name = "getHour" }
                                .invoke<String>() ?: return@afterHook
                            mResidentTimeHour.setClockRed(mHour, redMode)
                        }
                    }
                }
            }
        }
    }

    private fun getCharColor(view: TextView): Int? {
        val sp = SpannableString(view.text)
        val colorSpan = sp.getSpans(0, sp.length, ForegroundColorSpan::class.java)
        return if (colorSpan.isNotEmpty()) colorSpan[0].foregroundColor else null
    }

    @SuppressLint("DiscouragedApi")
    private fun TextView.setClockRed(format: String, redMode: String) {
        val mode = if (redMode == "1") 1 else if (redMode == "2") 2 else 0
        val sp = SpannableStringBuilder(format)
        if (mode != 2) {
            val charArray = format.toCharArray()
            for (i in charArray.indices) {
                if (charArray[i].toString() == "1") {
                    when (mode) {
                        0 -> {
                            val color = getCharColor(this)
                            if (color != null) sp.setSpan(
                                ForegroundColorSpan(color), i, i + 1, 34
                            )
                        }

                        1 -> {
                            val colorRes =
                                ThemeColorUtils(appClassLoader).let {
                                    it.getColor(17) ?: it.controlCenterRedOne
                                }
                            sp.setSpan(ForegroundColorSpan(colorRes), i, i + 1, 34)
                        }
                    }
                }
            }
        }
        text = sp
    }
}