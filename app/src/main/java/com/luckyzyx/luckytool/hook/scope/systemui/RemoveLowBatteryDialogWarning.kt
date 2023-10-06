package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveLowBatteryDialogWarning : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPowerNotificationWarnings
        VariousClass(
            "com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings", //C13
            "com.oplus.systemui.statusbar.notification.power.OplusPowerNotificationWarnings" //C14
        ).toClass().apply {
            method { name = "createSavePowerDialog" }.hook {
                intercept()
            }
            method { name = "createSuperSavePowerDialog" }.hook {
                intercept()
            }
        }
    }
}