package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveSmallWindowReplyWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source BaseNotificationContentInflater
        VariousClass(
            "com.oplusos.systemui.notification.base.BaseNotificationContentInflater", //C13
            "com.oplus.systemui.statusbar.NotificationListenerExtImpl" //C14
        ).hook {
            injectMember {
                method { name = "showSmallWindowReply" }
                replaceToTrue()
            }
        }
    }
}