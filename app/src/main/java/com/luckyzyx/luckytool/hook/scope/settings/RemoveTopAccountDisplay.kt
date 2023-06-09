package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object RemoveTopAccountDisplay : YukiBaseHooker() {
    override fun onHook() {
        //Source UserPreferenceController
        findClass("com.oplus.settings.feature.homepage.user.UserPreferenceController").hook {
            if (SDK >= A13) injectMember {
                method { name = "checkAvailable" }
                replaceToFalse()
            }
            else injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(3)
            }
        }
    }
}