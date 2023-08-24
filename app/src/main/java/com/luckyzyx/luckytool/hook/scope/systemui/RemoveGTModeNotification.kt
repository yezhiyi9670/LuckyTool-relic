package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveGTModeNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source GTUtils
        VariousClass(
            "com.oplusos.systemui.statusbar.util.GTUtils", //C13
            "com.oplus.systemui.statusbar.util.GTUtils" //C14
        ).hook {
            injectMember {
                method { name = "showOpenGtModeNotify";paramCount = 1 }
                intercept()
            }
        }
    }
}