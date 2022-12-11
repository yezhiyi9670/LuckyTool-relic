package com.luckyzyx.luckytool.hook.apps.wirelesssetting

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveHotspotPowerConsumptionNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source WifiApOverworkNotificationReceiver
        findClass("com.oplus.wirelesssettings.wifi.tether.WifiApOverworkNotificationReceiver").hook {
            injectMember {
                method {
                    name = "onReceive"
                    paramCount = 2
                }
                intercept()
            }
        }
    }
}