package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.RemoveBatteryNotify
import com.luckyzyx.luckytool.hook.scope.systemui.*
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object StatusBarNotify : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除充电完成通知
            if (prefs(XposedPrefs).getBoolean("remove_charging_completed", false)) {
                loadHooker(RemoveChargingCompleted)
            }
            //移除状态栏开发者选项警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_devmode", false)) {
                loadHooker(RemoveStatusBarDevMode)
            }
            //移除磁贴底部网络警告
            if (prefs(XposedPrefs).getBoolean("remove_statusbar_bottom_networkwarn", false)) {
                loadHooker(RemoveStatusBarBottomNetworkWarn)
            }
            //移除手电筒已开启通知
            if (prefs(XposedPrefs).getBoolean("remove_flashlight_open_notification", false)) {
                loadHooker(RemoveFlashlightOpenNotification)
            }
            //移除免打扰模式通知
            if (prefs(XposedPrefs).getBoolean("remove_do_not_disturb_mode_notification", false)) {
                loadHooker(RemoveDoNotDisturbModeNotification)
            }

        }
        loadApp("com.oplus.battery") {
            //移除电池通知
            loadHooker(RemoveBatteryNotify)
        }
    }
}