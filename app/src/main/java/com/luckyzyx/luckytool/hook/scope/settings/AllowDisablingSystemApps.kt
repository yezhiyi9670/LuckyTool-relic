package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object AllowDisablingSystemApps : YukiBaseHooker() {
    override fun onHook() {
        //Source AppButtonsPreferenceControllerAdaptor
        "com.oplus.settings.adaptor.AppButtonsPreferenceControllerAdaptor".toClass().apply {
            method { name = "setUninstallButtonEnabled" }.hook {
                before { args().first().setTrue() }
            }
        }
    }
}