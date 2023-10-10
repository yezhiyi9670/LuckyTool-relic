package com.luckyzyx.luckytool.hook.scope.battery

import android.app.NotificationManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
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
        val highPerformance =
            prefs(ModulePrefs).getBoolean("remove_high_performance_mode_notifications", false)
        //Channel PowerConsumptionOptimizationChannel / PowerConsumptionOptimizationChannelLow 17
        //power_consumption_optimization_title
        val highBatteryConsumption =
            prefs(ModulePrefs).getBoolean("remove_app_high_battery_consumption_warning", false)
        //Channel smart_charge_channel_id 20
//        val smartRapidCharge = prefs(ModulePrefs).getBoolean("remove_smart_rapid_charging_notification", false)

        //Source NotifyUtil
        val clsName = DexkitUtils.searchDexClass(
            "HookBatteryNotify", appInfo.sourceDir
        ) { dexKitBridge ->
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
        }

        if (highPerformance) {
            DexkitUtils.searchDexMethod(
                "HookBatteryNotify highPerformance", appInfo.sourceDir
            ) { dexKitBridge ->
                dexKitBridge.findMethod {
                    searchPackages(clsName)
                    matcher {
                        addUsingString("high_performance_channel_id")
                        addUsingString("ACTION_HIGH_PERFORMANCE")
                        addUsingNumber(5)
                    }
                }
            }?.firstOrNull()?.apply {
                className.toClass().apply {
                    method { name = methodName;emptyParam() }.hook {
                        intercept()
                    }
                }
            }
        }

        if (highBatteryConsumption) {
            clsName.toClass().apply {
                method {
                    param(StringClass, BooleanType)
                    paramCount = 2
                }.hookAll { intercept() }
            }
        }
    }
}