package com.luckyzyx.luckytool.hook.scope.wirelesssetting

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object RemoveHotspotPowerConsumptionNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source WifiApOverworkNotificationReceiver
//        tethering_wifi_ap_overwork_tips_content
        findClass("com.oplus.wirelesssettings.wifi.tether.WifiApOverworkNotificationReceiver").hook {
            injectMember {
                method {
                    param(ContextClass)
                    paramCount = 1
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}