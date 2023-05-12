package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object RemoveStatusBarBottomNetworkWarn : YukiBaseHooker() {
    override fun onHook() {
        var removeMode = prefs(ModulePrefs).getString("remove_control_center_networkwarn", "0")
        dataChannel.wait<String>("remove_control_center_networkwarn") { removeMode = it }
        //Source OplusQSSecurityText
        findClass("com.oplusos.systemui.qs.widget.OplusQSSecurityText").hook {
            injectMember {
                method { name = "handleClick" }
                if (removeMode == "1" || removeMode == "2") intercept()
            }
            injectMember {
                method { name = "handleRefreshState" }
                if (removeMode == "2") intercept()
            }
        }
    }
}