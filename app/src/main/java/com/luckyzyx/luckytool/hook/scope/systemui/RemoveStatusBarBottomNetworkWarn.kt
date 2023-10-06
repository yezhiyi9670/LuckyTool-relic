package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object RemoveStatusBarBottomNetworkWarn : YukiBaseHooker() {
    override fun onHook() {
        var removeMode = prefs(ModulePrefs).getString("remove_control_center_networkwarn", "0")
        dataChannel.wait<String>("remove_control_center_networkwarn") { removeMode = it }

        //Source OplusQSSecurityText
        VariousClass(
            "com.oplusos.systemui.qs.widget.OplusQSSecurityText", //C13
            "com.oplus.systemui.qs.widget.OplusQSSecurityText" //C14
        ).toClass().apply {
            method { name = "handleClick" }.hook {
                if (removeMode == "1" || removeMode == "2") intercept()
            }
            method { name = "handleRefreshState" }.hook {
                if (removeMode == "2") intercept()
            }
        }
    }
}