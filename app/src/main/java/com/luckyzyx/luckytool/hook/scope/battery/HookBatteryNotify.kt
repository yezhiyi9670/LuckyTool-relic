package com.luckyzyx.luckytool.hook.scope.battery

import android.app.NotificationManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
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
        DexkitUtils.searchDexClass("HookBatteryNotify", appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType(HandlerClass.name)
                        addForType(NotificationManager::class.java.name)
                    }
                    methods {
                        add {
                            paramTypes(StringClass.name, BooleanType.name)
                            returnType(UnitType.name)
                        }
                    }
                    usingStrings("NotifyUtil")
                }
            }
        }?.firstOrNull()?.className?.hook {
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
        }
    }
}