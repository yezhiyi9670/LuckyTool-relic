package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveDoNotDisturbModeNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source DndAlertHelper
        VariousClass(
            "com.oplusos.systemui.notification.helper.DndAlertHelper",
            "com.coloros.systemui.notification.helper.DndAlertHelper"
        ).hook {
            injectMember {
                method {
                    name = "sendNotificationWithEndtime"
                    paramCount = 1
                }
                intercept()
            }
        }
    }
}