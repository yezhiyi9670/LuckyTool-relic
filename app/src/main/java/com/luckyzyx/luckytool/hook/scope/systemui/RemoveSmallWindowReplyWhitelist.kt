package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveSmallWindowReplyWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source BaseNotificationContentInflater
        findClass("com.oplusos.systemui.notification.base.BaseNotificationContentInflater").hook {
            injectMember {
                method { name = "showSmallWindowReply" }
                replaceToTrue()
            }
        }
    }
}