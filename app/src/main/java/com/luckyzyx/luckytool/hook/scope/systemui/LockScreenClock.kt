package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.hook.utils.sysui.ClockSwitchHelper
import com.luckyzyx.luckytool.hook.utils.sysui.ThemeColorUtils
import com.luckyzyx.luckytool.hook.utils.sysui.WeatherInfoParseHelper
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import java.util.Calendar

object LockScreenClock : YukiBaseHooker() {

    lateinit var redMode: String
    var dualClock = false

    override fun onHook() {
        redMode = prefs(ModulePrefs).getString("lock_screen_clock_redone_mode", "0")
        dataChannel.wait<String>("lock_screen_clock_redone_mode") { redMode = it }
        dualClock = prefs(ModulePrefs).getBoolean("apply_lock_screen_dual_clock_redone", false)
        dataChannel.wait<Boolean>("apply_lock_screen_dual_clock_redone") { dualClock = it }

        //OPPO/Realme kgd_single_clock / kgd_dual_clock
        //Source SingleClockView kgd_single_clock
        VariousClass(
            "com.oplusos.systemui.keyguard.clock.SingleClockView", //C13
            "com.oplus.systemui.shared.clocks.SingleClockView" //C14
        ).hook {
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
        //OnePlus kgd_red_horizontal_single_clock / kgd_red_horizontal_dual_clock
        //Source RedTextClock
        VariousClass(
            "com.oplusos.systemui.keyguard.clock.RedTextClock", //C13
            "com.oplus.systemui.shared.clocks.RedTextClock" //C14
        ).hook {
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
        val weatherInfoClazz =
            if (SDK >= A14) "com.oplus.systemui.keyguard.clock.WeatherInfoParseHelper\$WeatherInfo".toClass()
            else "com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper\$WeatherInfo".toClass()
        //Source DualClockView kgd_dual_clock
        VariousClass(
            "com.oplusos.systemui.keyguard.clock.DualClockView", //C13
            "com.oplus.systemui.shared.clocks.DualClockView" //C14
        ).hook {
            injectMember {
                method { param { it.contains(weatherInfoClazz) } }.all()
                afterHook {
                    if (!dualClock) return@afterHook
                    val type: String = method.name.let {
                        if (it.contains("updateLocatedTime")) "LocatedTime"
                        else if (it.contains("updateResidentTime")) "ResidentTime"
                        else return@afterHook
                    }
                    when (type) {
                        "LocatedTime" -> {
                            val mLocatedTimeHour =
                                field { name = "mLocatedTimeHour" }.get(instance)
                                    .cast<TextView>()
                                    ?: return@afterHook
                            val mLocatedTimeInfo =
                                field { name = "mLocatedTimeInfo" }.get(instance).any()
                                    ?: return@afterHook
                            val mHour =
                                mLocatedTimeInfo.current().method { name = "getHour" }
                                    .invoke<String>() ?: return@afterHook
                            mLocatedTimeHour.setClockRed(mHour, redMode)
                        }

                        "ResidentTime" -> {
                            val mResidentTimeHour =
                                field { name = "mResidentTimeHour" }.get(instance)
                                    .cast<TextView>()
                                    ?: return@afterHook
                            val mResidentTimeInfo =
                                field { name = "mResidentTimeInfo" }.get(instance).any()
                                    ?: return@afterHook
                            val mHour =
                                mResidentTimeInfo.current().method { name = "getHour" }
                                    .invoke<String>() ?: return@afterHook
                            mResidentTimeHour.setClockRed(mHour, redMode)
                        }
                    }
                }
            }
        }
        if (SDK >= A13) loadHooker(HookRedDuanClock) else loadHooker(HookRedDuanClock12)
    }

    private fun TextView.setClockRed(format: String, redMode: String) {
        val sp = SpannableStringBuilder(format)
        if (redMode == "1") {
            for (i in format.indices) {
                if (format[i].toString() == "1") {
                    val colorRes = ThemeColorUtils(appClassLoader).let {
                        it.getColor(17) ?: it.controlCenterRedOne
                    }
                    sp.setSpan(ForegroundColorSpan(colorRes), i, i + 1, 34)
                }
            }
        }
        text = sp
    }

    private object HookRedDuanClock : YukiBaseHooker() {
        override fun onHook() {
            val timeInfoClazz =
                if (SDK >= A14) "com.oplus.systemui.keyguard.clock.WeatherInfoParseHelper\$TimeInfo".toClass()
                else "com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper\$TimeInfo".toClass()
            //Source RedHorizontalDualClockView kgd_red_horizontal_dual_clock
            VariousClass(
                "com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView", //C13
                "com.oplus.systemui.shared.clocks.RedHorizontalDualClockView" //C14
            ).hook {
                injectMember {
                    method { param { it.contains(timeInfoClazz) };paramCount = 3 }.all()
                    afterHook {
                        if (!dualClock) return@afterHook
                        val type: String = method.name.let {
                            if (it.contains("updateLocateTime")) "LocateTime"
                            else if (it.contains("updateResidentTime")) "ResidentTime"
                            else return@afterHook
                        }
                        val view = args().first().any() ?: return@afterHook
                        when (type) {
                            "LocateTime" -> {
                                val mLocatedTimeHour =
                                    view.current()
                                        .field { name = "mTvHorizontalLocateClockHour" }
                                        .cast<TextView>() ?: return@afterHook
                                val mLocatedTimeInfo = args().last().any() ?: return@afterHook
                                val mHour =
                                    mLocatedTimeInfo.current().method { name = "getHour" }
                                        .invoke<String>() ?: return@afterHook
                                mLocatedTimeHour.setClockRed(mHour, redMode)
                            }

                            "ResidentTime" -> {
                                val mResidentTimeHour =
                                    view.current()
                                        .field { name = "mTvHorizontalResidentClockHour" }
                                        .cast<TextView>() ?: return@afterHook
                                val mResidentTimeInfo = args().last().any() ?: return@afterHook
                                val mHour =
                                    mResidentTimeInfo.current().method { name = "getHour" }
                                        .invoke<String>() ?: return@afterHook
                                mResidentTimeHour.setClockRed(mHour, redMode)
                            }
                        }
                    }
                }
            }
        }
    }

    private object HookRedDuanClock12 : YukiBaseHooker() {
        override fun onHook() {
            //Source RedHorizontalDualClockView kgd_red_horizontal_dual_clock
            findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView").hook {
                injectMember {
                    method { name = "updateLocateTime" }
                    afterHook {
                        if (!dualClock) return@afterHook
                        val mContext =
                            instanceClass.current().field { name = "mContext" }.cast<Context>()
                                ?: return@afterHook
                        val mLocatedTimeHour =
                            instanceClass.current().field { name = "mTvHorizontalLocateClockHour" }
                                .cast<TextView>() ?: return@afterHook
                        val mLocatedTimeInfo =
                            WeatherInfoParseHelper(appClassLoader).getLocalTimeInfo(mContext)
                                ?: return@afterHook
                        val mHour =
                            mLocatedTimeInfo.current().method { name = "getHour" }.invoke<String>()
                                ?: return@afterHook
                        mLocatedTimeHour.setClockRed(mHour, redMode)
                    }
                }
                injectMember {
                    method { name = "updateResidentTime" }
                    afterHook {
                        if (!dualClock) return@afterHook
                        val mContext =
                            instanceClass.current().field { name = "mContext" }.cast<Context>()
                                ?: return@afterHook
                        val mResidentTimeHour = instanceClass.current()
                            .field { name = "mTvHorizontalResidentClockHour" }.cast<TextView>()
                            ?: return@afterHook
                        val info = ClockSwitchHelper(appClassLoader).let {
                            it.getInstance(mContext)?.let { its -> it.getResidentWeatherInfo(its) }
                        } ?: WeatherInfoParseHelper(appClassLoader).weatherInfoClazz.buildOf {
                            emptyParam()
                        }
                        val timeZone = info?.current()?.method { name = "getTimeZone" }?.invoke<String>() ?: "0.0"
                        val mResidentTimeInfo =
                            WeatherInfoParseHelper(appClassLoader).getResidentTimeInfo(
                                mContext, timeZone
                            ) ?: return@afterHook
                        val mHour =
                            mResidentTimeInfo.current().method { name = "getHour" }.invoke<String>()
                                ?: return@afterHook
                        mResidentTimeHour.setClockRed(mHour, redMode)
                    }
                }
            }
        }
    }
}
