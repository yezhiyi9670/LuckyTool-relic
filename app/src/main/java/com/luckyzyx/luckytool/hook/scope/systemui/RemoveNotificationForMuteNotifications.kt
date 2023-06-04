package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveNotificationForMuteNotifications : YukiBaseHooker() {
    override fun onHook() {
        //Source NoDisturbController
        findClass("com.oplusos.systemui.statusbar.controller.NoDisturbController").hook {
            injectMember {
                method { name = "sendNotification" }
                intercept()
            }
        }
    }
}