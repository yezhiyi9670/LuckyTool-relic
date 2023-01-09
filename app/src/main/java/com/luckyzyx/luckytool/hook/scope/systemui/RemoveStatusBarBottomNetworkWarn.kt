package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveStatusBarBottomNetworkWarn : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusQSSecurityText
        findClass("com.oplusos.systemui.qs.widget.OplusQSSecurityText").hook {
            injectMember {
                method {
                    name = "handleRefreshState"
                }
                intercept()
            }
        }
    }
}