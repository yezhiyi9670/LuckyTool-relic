package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookNotificationManager : YukiBaseHooker() {
    override fun onHook() {
        //Source_ext oplus-wifi-service OplusTetheringNotification showSoftapEnabledDurationNotification
        //Channel DurationNotification -> Notification id -> 4
        val hotspotPowerConsumption =
            prefs(ModulePrefs).getBoolean("remove_hotspot_power_consumption_notification", false)

        //Source NotificationManager
        findClass("android.app.NotificationManager").hook {
            injectMember {
                method {
                    name = "notify"
                    paramCount = 3
                }
                beforeHook {
                    when (args(1).int()) {
                        4 -> if (hotspotPowerConsumption) resultNull()
                    }
                }
            }
        }
    }
}
