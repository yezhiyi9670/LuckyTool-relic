package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHiddenEntrance
import com.luckyzyx.luckytool.hook.scope.battery.HookThermalController
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookBattery : YukiBaseHooker() {
    override fun onHook() {
        //屏幕省电,电池健康
        loadHooker(BatteryHiddenEntrance)

        //移除高温限制
        if (prefs(ModulePrefs).getBoolean("remove_high_temperature_limit", false)) {
            loadHooker(HookThermalController)
        }

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