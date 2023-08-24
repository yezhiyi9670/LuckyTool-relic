package com.luckyzyx.luckytool.hook.scope.battery

import android.app.NotificationManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookBatteryNotify : YukiBaseHooker() {
    override fun onHook() {
        //Channel high_performance_channel_id 5
//        val highPerformance = prefs(ModulePrefs).getBoolean("remove_high_performance_mode_notifications", false)
        //Channel PowerConsumptionOptimizationChannel / PowerConsumptionOptimizationChannelLow 17
        //power_consumption_optimization_title
        val highBatteryConsumption =
            prefs(ModulePrefs).getBoolean("remove_app_high_battery_consumption_warning", false)
        //Channel smart_charge_channel_id 20
//        val smartRapidCharge = prefs(ModulePrefs).getBoolean("remove_smart_rapid_charging_notification", false)

        //Source NotifyUtil
        searchClass {
            from(
                "com.oplus.common.notification", "com.oplus.a.g",
                "c4", "a4", "i5", "g4", "z3", "y5", "f6"
            ).absolute()
            constructor { paramCount = 1 }.count(1)
            field { type = ContextClass }.count(1)
            field { type = NotificationManager::class.java }.count(1)
            field { type = HandlerClass }.count(1)
            method { param(StringClass, BooleanType) }.count(4)
        }.get()?.hook {
            injectMember {
                method {
                    param(StringClass, BooleanType)
                    paramCount = 2
                }.all()
                if (highBatteryConsumption) intercept()
            }
//            injectMember {
//                constructor {
//                    paramCount = 1
//                }
//                afterHook {
//                    field {
//                        type = NotificationManager::class.java
//                    }.get(instance).cast<NotificationManager>()?.javaClass?.hook {
//                        injectMember {
//                            method {
//                                name = "notify"
//                                param(StringClass, IntType, NotificationClass)
//                                paramCount = 3
//                            }
//                            beforeHook {
//                                when (args(1).cast<Int>()) {
//                                    5 -> if (highPerformance) resultNull()
//                                    20 -> if (smartRapidCharge) resultNull()
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveBatteryNotify")
    }
}