package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object ForceDisplayBottomGoogleSettings : YukiBaseHooker() {
    override fun onHook() {
        //Source GooglePreferenceController
        findClass("com.oplus.settings.feature.homepage.controller.GooglePreferenceController").hook {
            injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(0)
            }
        }
    }
}