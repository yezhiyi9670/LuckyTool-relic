package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveLockScreenCloseNotificationButton : YukiBaseHooker() {
    override fun onHook() {
        //Source NotificationPanelViewExt
        findClass("com.oplusos.systemui.notification.extend.NotificationPanelViewExt").hook {
            injectMember {
                method {
                    name = "setNotificationCloseButton"
                    paramCount = 1
                }
                intercept()
            }
        }
    }
}