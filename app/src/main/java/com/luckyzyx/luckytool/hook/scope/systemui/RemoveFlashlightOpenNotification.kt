package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveFlashlightOpenNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source FlashlightNotification
        VariousClass(
            "com.oplusos.systemui.flashlight.FlashlightNotification", //C13
            "com.oplus.systemui.statusbar.notification.flashlight.FlashlightNotification" //C14
        ).hook {
            injectMember {
                method { name = "sendNotification";paramCount = 1 }
                intercept()
            }
        }
    }
}