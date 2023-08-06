package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object RemoveTopAccountDisplay : YukiBaseHooker() {
    override fun onHook() {
        //Source UserPreferenceController
        findClass("com.oplus.settings.feature.homepage.user.UserPreferenceController").hook {
            injectMember {
                method {
                    name = if (SDK >= A13) "checkAvailable"
                    else "getAvailabilityStatus"
                }
                if (SDK >= A13) replaceToFalse() else replaceTo(3)
            }
        }
    }
}