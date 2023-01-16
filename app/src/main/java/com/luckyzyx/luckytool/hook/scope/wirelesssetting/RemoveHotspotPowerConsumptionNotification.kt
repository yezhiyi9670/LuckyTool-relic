package com.luckyzyx.luckytool.hook.scope.wirelesssetting

import android.app.NotificationManager
import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveHotspotPowerConsumptionNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source WifiApOverworkNotificationReceiver tethering_wifi_ap_overwork_tips_content
        //Channel 10999 wifi
        findClass("com.oplus.wirelesssettings.wifi.tether.WifiApOverworkNotificationReceiver").hook {
            injectMember {
                method {
                    name = "onReceive"
                    paramCount = 2
                }
                beforeHook {
                    val context = args(0).cast<Context>()
                    val notificationManager =
                        context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.javaClass.hook {
                        injectMember {
                            method {
                                name = "notify"
                                paramCount = 2
                            }
                            beforeHook {
                                val id = args(0).cast<Int>()
                                if (id == 109999) resultNull()
                            }
                        }
                    }
                }
            }
        }
    }
}