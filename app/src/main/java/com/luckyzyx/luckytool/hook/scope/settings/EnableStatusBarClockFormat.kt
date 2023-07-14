package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object EnableStatusBarClockFormat : YukiBaseHooker() {
    override fun onHook() {
        //Source RmStatusbarClockPreferenceController
        findClass("com.oplus.settings.feature.notification.controller.RmStatusbarClockPreferenceController").hook {
            injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(0)
            }
        }
    }
}