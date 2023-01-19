package com.luckyzyx.luckytool.hook.scope.oplus

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object RemoveHotspotPowerConsumptionNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusTetheringNotification oplus-wifi-service
        //Channel DurationNotification
        //Notification id -> 4
        findClass("com.oplus.server.wifi.hotspot.OplusTetheringNotification").hook {
            injectMember {
                method {
                    name = "showSoftapEnabledDurationNotification"
                }
                if (prefs(XposedPrefs).getBoolean(
                        "remove_hotspot_power_consumption_notification", false
                    )
                ) intercept()
            }
        }
    }
}