package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object RemoveTopAccountDisplay : YukiBaseHooker() {
    override fun onHook() {
        //Source UserPreferenceController
        "com.oplus.settings.feature.homepage.user.UserPreferenceController".toClass().apply {
            method {
                name = if (SDK >= A13) "checkAvailable"
                else "getAvailabilityStatus"
            }.hook {
                if (SDK >= A13) replaceToFalse() else replaceTo(3)
            }
        }
    }
}