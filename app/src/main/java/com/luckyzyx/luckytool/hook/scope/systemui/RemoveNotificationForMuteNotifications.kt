package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveNotificationForMuteNotifications : YukiBaseHooker() {
    override fun onHook() {
        //Source NoDisturbController
        VariousClass(
            "com.oplusos.systemui.statusbar.controller.NoDisturbController",
            "com.oplus.systemui.statusbar.controller.NoDisturbController" //C14
        ).hook {
            injectMember {
                method { name = "sendNotification" }
                intercept()
            }
        }
    }
}