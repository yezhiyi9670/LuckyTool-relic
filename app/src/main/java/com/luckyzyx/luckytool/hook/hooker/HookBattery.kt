package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHealthDataTips
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

        //电池健康数据提示
        if (SDK >= A13 && prefs(ModulePrefs).getBoolean("fix_battery_health_data_display", false)) {
            loadHooker(BatteryHealthDataTips)
        }

        //charge_protection_switch_state
        //smart_long_charge_protection_switch_state

    }
}