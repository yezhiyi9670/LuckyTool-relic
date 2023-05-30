package com.luckyzyx.luckytool.hook.scope.uiengine

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveAodNotificationWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source NotificationView -> BaseView
        findClass("com.oplus.egview.widget.BaseView").hook {
            injectMember {
                method { name = "isExpRegion" }
                replaceToTrue()
            }
        }
    }
}