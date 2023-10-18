package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryFeatureProvider
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHealthDataTips
import com.luckyzyx.luckytool.hook.scope.battery.HookBatteryNotify
import com.luckyzyx.luckytool.hook.scope.battery.RemoveBatteryTemperatureControl
import com.luckyzyx.luckytool.hook.scope.battery.UnlockStartupLimit
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookBattery : YukiBaseHooker() {
    override fun onHook() {
        //BatteryFeatureProvider
        //屏幕省电 电池健康
        loadHooker(BatteryFeatureProvider)

        //电池通知
        loadHooker(HookBatteryNotify)

        //移除自启数量限制
        if (prefs(ModulePrefs).getBoolean("unlock_startup_limit", false)) {
            if (SDK >= A13) loadHooker(UnlockStartupLimit)
        }

        //移除电池温度控制
        if (prefs(ModulePrefs).getBoolean("remove_battery_temperature_control", false)) {
            loadHooker(RemoveBatteryTemperatureControl)
        }

        //电池健康数据提示
        if (SDK >= A13 && prefs(ModulePrefs).getBoolean("fix_battery_health_data_display", false)) {
            loadHooker(BatteryHealthDataTips)
        }

        //charge_protection_switch_state
        //smart_long_charge_protection_switch_state

    }
}