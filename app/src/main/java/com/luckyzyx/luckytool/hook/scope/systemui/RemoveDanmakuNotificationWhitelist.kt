package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object RemoveDanmakuNotificationWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source DanmakuHelper
        VariousClass(
            "com.oplusos.systemui.notification.helper.DanmakuHelper", //C13
            "com.oplus.systemui.statusbar.notification.helper.HeadsUpHelper" //C14
        ).toClass().apply {
            method {
                name = if (SDK >= A14) "isPkgBarrageEnable"
                else "isSupportDanmaku"
            }.hook { replaceToTrue() }
        }
    }
}