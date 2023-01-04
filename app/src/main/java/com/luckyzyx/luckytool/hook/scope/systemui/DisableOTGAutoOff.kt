package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class DisableOTGAutoOff : YukiBaseHooker() {
    override fun onHook() {
        // Search OtgHelper 600000
        findClass("com.oplusos.systemui.notification.helper.OtgHelper").hook {
            injectMember {
                method {
                    name = "setAutoCloseAlarm"
                }
                intercept()
            }
        }
    }
}