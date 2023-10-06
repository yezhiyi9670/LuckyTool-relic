package com.luckyzyx.luckytool.hook.scope.uiengine

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveAodNotificationWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source NotificationView -> BaseView
        "com.oplus.egview.widget.BaseView".toClass().apply {
            method { name = "isExpRegion" }.hook {
                replaceToTrue()
            }
        }
    }
}