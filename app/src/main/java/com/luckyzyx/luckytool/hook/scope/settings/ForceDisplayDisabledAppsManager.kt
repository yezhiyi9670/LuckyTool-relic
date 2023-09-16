package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object ForceDisplayDisabledAppsManager : YukiBaseHooker() {
    override fun onHook() {
        //Source DisabledAppsPreferenceController
        findClass("com.android.settings.applications.disableapps.DisabledAppsPreferenceController").hook {
            injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(0)
            }
        }
    }
}