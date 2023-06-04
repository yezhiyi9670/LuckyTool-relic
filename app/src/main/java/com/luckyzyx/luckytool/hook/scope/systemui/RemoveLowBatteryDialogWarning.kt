package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveLowBatteryDialogWarning : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPowerNotificationWarnings
        findClass("com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings").hook {
            injectMember {
                method { name = "createSavePowerDialog" }
                intercept()
            }
            injectMember {
                method { name = "createSuperSavePowerDialog" }
                intercept()
            }
        }
    }
}