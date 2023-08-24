package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object RemoveVPNActiveNotification : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_vpn_active_notification", false)
        // Source OplusVpnHelper
        VariousClass(
            "com.android.server.connectivity.VpnExtImpl", //C12 C13 C14
            "com.android.server.connectivity.OplusVpnHelper"
        ).hook {
            injectMember {
                method { name = "showNotification" }
                if (isEnable) intercept()
            }
        }
    }
}