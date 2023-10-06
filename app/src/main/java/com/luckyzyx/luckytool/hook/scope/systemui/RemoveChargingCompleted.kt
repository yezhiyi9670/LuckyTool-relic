package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveChargingCompleted : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPowerNotificationWarnings
        VariousClass(
            "com.coloros.systemui.notification.power.ColorosPowerNotificationWarnings", //A11
            "com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings",
            "com.coloros.systemui.notification.power.ColorosPowerNotificationWarnings",
            "com.oplus.systemui.statusbar.notification.power.OplusPowerNotificationWarnings" //C14
        ).toClass().apply {
            method { name = "showChargeErrorDialog";paramCount = 1 }.hook {
                before { if (args().first().int() == 7) resultNull() }
            }
        }
    }
}