package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class RemoveVPNActiveNotification : YukiBaseHooker() {
    override fun onHook() {
        // Source OplusVpnHelper
        var isEnable = prefs(XposedPrefs).getBoolean("remove_vpn_active_notification", false)
        dataChannel.wait<Boolean>(key = "remove_vpn_active_notification") { isEnable = it }
        VariousClass(
            "com.android.server.connectivity.VpnExtImpl",
            "com.android.server.connectivity.OplusVpnHelper"
        ).hook {
            injectMember {
                method {
                    name = "showNotification"
                }
                if (isEnable) intercept()
            }
        }
    }
}