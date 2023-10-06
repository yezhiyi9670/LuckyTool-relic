package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object ForceDisplayDisabledAppsManager : YukiBaseHooker() {
    override fun onHook() {
        //Source DisabledAppsPreferenceController
        "com.android.settings.applications.disableapps.DisabledAppsPreferenceController".toClass()
            .apply {
                method { name = "getAvailabilityStatus" }.hook {
                    replaceTo(0)
                }
            }
    }
}