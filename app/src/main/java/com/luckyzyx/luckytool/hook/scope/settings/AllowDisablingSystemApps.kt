package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object AllowDisablingSystemApps : YukiBaseHooker() {
    override fun onHook() {
        //Source AppButtonsPreferenceControllerAdaptor
        findClass("com.oplus.settings.adaptor.AppButtonsPreferenceControllerAdaptor").hook {
            injectMember {
                method { name = "setUninstallButtonEnabled" }
                beforeHook { args().first().setTrue() }
            }
        }
    }
}