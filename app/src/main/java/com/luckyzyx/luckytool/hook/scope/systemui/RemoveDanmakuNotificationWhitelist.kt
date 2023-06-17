package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveDanmakuNotificationWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source DanmakuHelper
        findClass("com.oplusos.systemui.notification.helper.DanmakuHelper").hook {
            injectMember {
                method { name = "isSupportDanmaku" }
                replaceToTrue()
            }
        }
    }
}