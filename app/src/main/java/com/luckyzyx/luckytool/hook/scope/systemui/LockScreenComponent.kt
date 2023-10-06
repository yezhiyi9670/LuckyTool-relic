package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.dp

object LockScreenComponent : YukiBaseHooker() {
    override fun onHook() {
        val isCenter = prefs(ModulePrefs).getBoolean("set_lock_screen_centered", false)
        val userTypeface =
            prefs(ModulePrefs).getBoolean("lock_screen_clock_use_user_typeface", false)

        //Source RedHorizontalSingleClockView
        VariousClass(
            "com.oplusos.systemui.keyguard.clock.RedHorizontalSingleClockView", //C13
            "com.oplus.systemui.shared.clocks.RedHorizontalSingleClockView" //C14
        ).toClass().apply {
            method { name = "onFinishInflate" }.hook {
                after {
                    if (!isCenter) return@after
                    instance<LinearLayout>().setPadding(0, 20.dp, 0, 0)

                    field { name = "mTvWeek" }.get(instance).cast<TextView>()
                        ?.setCenterHorizontally()

//                    field { name = "mTvHour" }.get(instance).cast<TextView>()
//                    field { name = "mTvMinute" }.get(instance).cast<TextView>()

                    (field { name = "mTvColon" }.get(instance)
                        .cast<TextView>()?.parent as RelativeLayout).setCenterHorizontally()

                    field { name = "mTvDate" }.get(instance).cast<TextView>()
                        ?.setCenterHorizontally()

                    field { name = "mTvLunarCalendar" }.get(instance).cast<TextView>()
                        ?.setCenterHorizontally()

                    field { name = "mTvExtraContent" }.get(instance).cast<TextView>()
                        ?.setCenterHorizontally()
                }
            }
            method { name = "setTextFont" }.hook {
                if (userTypeface) intercept()
            }
        }
        //Source RedHorizontalDualClockView
        VariousClass(
            "com.oplusos.systemui.keyguard.clock.RedHorizontalDualClockView", //C13
            "com.oplus.systemui.shared.clocks.RedHorizontalDualClockView" //C14
        ).toClass().apply {
            method { name = "setTextFont" }.hook {
                if (userTypeface) intercept()
            }
        }
    }

    private fun View.setCenterHorizontally() {
        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
    }
}