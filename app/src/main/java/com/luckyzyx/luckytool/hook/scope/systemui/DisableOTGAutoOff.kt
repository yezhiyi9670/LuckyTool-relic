package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object DisableOTGAutoOff : YukiBaseHooker() {
    override fun onHook() {
        // Search OtgHelper 600000
        VariousClass(
            "com.oplusos.systemui.notification.helper.OtgHelper", //C13
            "com.oplus.systemui.qs.helper.OtgHelper" //C14
        ).hook {
            injectMember {
                method { name = "setAutoCloseAlarm" }
                intercept()
            }
        }
    }
}