package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveLowBatteryDialogWarning : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPowerNotificationWarnings
        VariousClass(
            "com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings", //C13
            "com.oplus.systemui.statusbar.notification.power.OplusPowerNotificationWarnings" //C14
        ).hook {
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