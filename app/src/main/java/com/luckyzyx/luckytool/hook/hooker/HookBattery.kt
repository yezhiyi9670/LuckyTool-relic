package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHiddenEntrance
import com.luckyzyx.luckytool.hook.scope.battery.HookThermalController
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookBattery : YukiBaseHooker() {
    override fun onHook() {
        //屏幕省电,电池健康
        loadHooker(BatteryHiddenEntrance)

        //移除高温限制
        if (prefs(ModulePrefs).getBoolean("remove_high_temperature_limit", false)) {
            loadHooker(HookThermalController)
        }

        //high_temperature_dialog_title
        //high_temperature_dialog_message
        //Source ThermalHandler
//        searchClass {
//            from("s4").absolute()
//            field { type = ContextClass }.count(2)
//            field { type = IntType }.count(4)
//            field { type = PowerManagerClass }.count(1)
//            field { type = SharedPreferencesClass }.count(1)
//            field { type = BroadcastReceiverClass }.count(1)
//            field { type = HandlerClass }.count(1)
//            constructor {
//                paramCount = 3
//            }.count(1)
//            method {
//                name = "handleMessage"
//                param(MessageClass)
//            }.count(1)
//            method {
//                param(IntType, IntType)
//            }.count(1)
//            method {
//                param(ContextClass)
//            }.count(2)
//            method {
//                param(ContextClass)
//                returnType = ContextClass
//            }.count(1)
//        }.get()?.hook {
//            injectMember {
//                constructor {
//                    paramCount = 3
//                }
//                afterHook {
//                    field {
//                        type = HandlerClass
//                    }.get(instance).set(Handler(Looper.getMainLooper()))
//                }
//            }
//        }

        //BatteryHealthFragment
        //max_capacity_data
        //battery_health_obtain_fail -> 获取失败

//        val binder = ServiceManagerUtils(appClassLoader).getService("guardelfthermalcontrol")
//        loggerD(msg = "binder -> ${binder != null}")
//
//        val instance = binder?.let { IGuardElfThermalControlUtils(appClassLoader).getInstance(it) }
//        loggerD(msg = "instance -> ${instance != null}")
//
//        val health = IGuardElfThermalControlUtils(appClassLoader).clazz.method {
//            name = "getUIsohValue"
//            emptyParam()
//        }.get(instance).invoke<Int>()
//        loggerD(msg = "health -> $health")
    }
}