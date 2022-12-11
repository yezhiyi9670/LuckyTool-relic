package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.battery.RemoveAppHighBatteryConsumptionWarning
import com.luckyzyx.luckytool.hook.apps.battery.RemoveHighPerformanceModeNotifications
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.hook.apps.wirelesssetting.RemoveHotspotPowerConsumptionNotification
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class StatusBarNotice : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除充电完成通知
            if (prefs(XposedPrefs).getBoolean("remove_charging_completed", false)) {
                loadHooker(RemoveChargingCompleted())
            }
            //移除状态栏开发者选项警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_devmode", false)) {
                loadHooker(RemoveStatusBarDevMode())
            }
            //移除磁贴底部网络警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_bottom_networkwarn", false)) {
                loadHooker(RemoveStatusBarBottomNetworkWarn())
            }
            //移除手电筒已开启通知
            if (prefs(XposedPrefs).getBoolean("remove_flashlight_open_notification", false)) {
                loadHooker(RemoveFlashlightOpenNotification())
            }
            //移除免打扰模式通知
            if (prefs(XposedPrefs).getBoolean("remove_do_not_disturb_mode_notification", false)) {
                loadHooker(RemoveDoNotDisturbModeNotification())
            }

        }
        loadApp("com.oplus.wirelesssettings") {
            //移除个人热点耗电通知
            if (prefs(XposedPrefs).getBoolean("remove_hotspot_power_consumption_notification", false)) {
                loadHooker(RemoveHotspotPowerConsumptionNotification())
            }
        }
        loadApp("com.oplus.battery") {
            //移除高性能模式通知
            if (prefs(XposedPrefs).getBoolean("remove_high_performance_mode_notifications", false)) {
                loadHooker(RemoveHighPerformanceModeNotifications())
            }
            //移除应用耗电异常优化警告
            if (prefs(XposedPrefs).getBoolean("remove_app_high_battery_consumption_warning", false)) {
                loadHooker(RemoveAppHighBatteryConsumptionWarning())
            }
        }
    }
}