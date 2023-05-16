package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveTopAccountDisplay : YukiBaseHooker() {
    override fun onHook() {
        //Source UserPreferenceController
        findClass("com.oplus.settings.feature.homepage.user.UserPreferenceController").hook {
            injectMember {
                method { name = "checkAvailable" }
                replaceToFalse()
            }
        }
    }
}