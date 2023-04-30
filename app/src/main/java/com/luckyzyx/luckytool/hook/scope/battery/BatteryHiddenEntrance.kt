package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContentResolverClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object BatteryHiddenEntrance : YukiBaseHooker() {
    override fun onHook() {
        val openScreenPowerSave = prefs(ModulePrefs).getBoolean("open_screen_power_save", false)
        val openBatteryHealth = prefs(ModulePrefs).getBoolean("open_battery_health", false)
        val performanceModeStandbyOptimization =
            prefs(ModulePrefs).getBoolean("performance_mode_and_standby_optimization", false)
        val openBatteryOptimize = false
        //Source AppFeatureProviderUtils
        searchClass {
            from(
                "com.oplus.coreapp.appfeature",
                "com.oplus.b.a",
                "k4",
                "i4",
                "r5",
                "o4",
                "h4"
            ).absolute()
            method {
//                name = "isFeatureSupport"
                param(ContentResolverClass, StringClass)
                returnType = BooleanType
            }.count(1)
            method {
//                name = "getInt"
                param(ContentResolverClass, StringClass, IntType)
                returnType = IntType
            }.count(1)
            method {
//                name = "getBoolean"
                param(ContentResolverClass, StringClass, BooleanType)
                returnType = BooleanType
            }.count(1)
            method {
//                name = "getStringList"
//                name = "getStringListForFeature"
                param(ContentResolverClass, StringClass)
                returnType = ListClass
            }.count(1..2)
        }.get()?.hook {
            injectMember {
                method {
//                    name = "isFeatureSupport"
                    param(ContentResolverClass, StringClass)
                    returnType = BooleanType
                }
                beforeHook {
                    when (args(1).cast<String>()) {
                        //屏幕省电
                        "com.oplus.battery.cabc_level_dynamic_enable" -> if (openScreenPowerSave) resultTrue()
                        //电池健康
                        "os.charge.settings.batterysettings.batteryhealth" -> if (openBatteryHealth) resultTrue()
                        //电池引擎优化
                        "com.oplus.battery.life.mode.notificate" -> if (openBatteryOptimize) resultTrue()
                        //性能模式/待机优化
                        "com.android.settings.device_rm" -> if (performanceModeStandbyOptimization) resultTrue()
                    }
                }
            }
            injectMember {
                method {
//                    name = "getInt"
                    param(ContentResolverClass, StringClass, IntType)
                    returnType = IntType
                }
                beforeHook {
                    val array = arrayOf(args(1).cast<String>(), args(2).cast<Int>())
                    //电池引擎优化
                    if (array[0] == "com.oplus.battery.life.mode.notificate" && array[1] == 0) {
                        if (openBatteryOptimize) result = 1
                    }
                }
            }
            injectMember {
                method {
//                    name = "getBoolean"
                    param(ContentResolverClass, StringClass, BooleanType)
                    returnType = BooleanType
                }
                beforeHook {
                    val array = arrayOf(args(1).cast<String>(), args(2).cast<Boolean>())
                    when (array[0]) {
                        //睡眠待机优化
                        "com.oplus.battery.disable_deep_sleep" -> resultFalse()
                    }
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> BatteryHiddenEntrance")

        //res/xml/battery_health_preference.xml
        //BatteryHealthDataPreference
    }
}