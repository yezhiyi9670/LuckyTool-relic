package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.CustomClock
import com.luckyzyx.luckytool.utils.tools.XposedPrefs


object StatusBarClock : YukiBaseHooker() {
    override fun onHook() {
        //状态栏时钟
        if (prefs(XposedPrefs).getBoolean("statusbar_clock_enable", false)) {
            loadHooker(CustomClock)
        }

        //com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment
        //com.android.systemui.statusbar.phone.PhoneStatusBarViewController
        //res/layout/status_bar.xml
//        findClass("com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment").hook {
//            injectMember {
//                method {
//                    name = "onViewCreated"
//                }
//                afterHook {
//                    field {
//                        name = "mSystemIconArea"
//                    }.get(instance).cast<LinearLayout>()?.apply {
//                        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
//                            width = LayoutParams.WRAP_CONTENT
//                        }
//                    }
//                }
//            }
//        }
    }
}