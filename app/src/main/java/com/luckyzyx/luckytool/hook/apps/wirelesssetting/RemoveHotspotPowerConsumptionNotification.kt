package com.luckyzyx.luckytool.hook.apps.wirelesssetting

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveHotspotPowerConsumptionNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source WifiApOverworkNotificationReceiver
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