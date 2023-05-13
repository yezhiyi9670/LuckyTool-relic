package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveGTModeNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source GTUtils
        findClass("com.oplusos.systemui.statusbar.util.GTUtils").hook {
            injectMember {
                method {
                    name = "showOpenGtModeNotify"
                    paramCount = 1
                }
                intercept()
            }
        }
    }
}