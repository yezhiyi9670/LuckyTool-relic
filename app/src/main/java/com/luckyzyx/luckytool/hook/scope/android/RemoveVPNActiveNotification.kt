package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object RemoveVPNActiveNotification : YukiBaseHooker() {
    override fun onHook() {
        // Source OplusVpnHelper
        if (!prefs(XposedPrefs).getBoolean("remove_vpn_active_notification", false)) return
        VariousClass(
            "com.android.server.connectivity.VpnExtImpl", //C12
            "com.android.server.connectivity.OplusVpnHelper"
        ).hook {
            injectMember {
                method {
                    name = "showNotification"
                }
                intercept()
            }
        }
    }
}