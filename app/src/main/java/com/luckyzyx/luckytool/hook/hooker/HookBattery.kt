package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHiddenEntrance
import com.luckyzyx.luckytool.hook.scope.battery.HookThermalController
import com.luckyzyx.luckytool.hook.scope.battery.LauncherHighTempreatureProtection
import com.luckyzyx.luckytool.hook.scope.battery.UnlockStartupLimit
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookBattery : YukiBaseHooker() {
    override fun onHook() {
        //移除自启数量限制
        if (SDK >= A13 && prefs(ModulePrefs).getBoolean("unlock_startup_limit", false)) {
            loadHooker(UnlockStartupLimit)
        }

        //屏幕省电,电池健康
        loadHooker(BatteryHiddenEntrance)

        //移除高温限制
        if (prefs(ModulePrefs).getBoolean("remove_high_temperature_limit", false)) {
            loadHooker(HookThermalController)
            loadHooker(LauncherHighTempreatureProtection)
        }

        //charge_protection_switch_state
        //smart_long_charge_protection_switch_state

        //BatteryHealthFragment
        //max_capacity_data
        //battery_health_obtain_fail -> 获取失败

//        val binder = ServiceManagerUtils(appClassLoader).getService("guardelfthermalcontrol")
//        loggerD(msg = "binder -> ${binder != null}")
//
//        val instance = binder?.let { IGuardElfThermalControlUtils(appClassLoader).getInstance(it) }
//        loggerD(msg = "instance -> ${instance != null}")
//
//        val health = IGuardElfThermalControlUtils(appClassLoader).getUIsohValue(instance)
//        loggerD(msg = "health -> $health")
    }
}