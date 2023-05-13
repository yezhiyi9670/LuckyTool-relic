package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.RemoveBatteryNotify
import com.luckyzyx.luckytool.hook.scope.phonemanager.RemoveVirusRiskNotificationInPhoneManager
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveChargingCompleted
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveDoNotDisturbModeNotification
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveFlashlightOpenNotification
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveGTModeNotification
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveNotificationForMuteNotifications
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveStatusBarDevMode
import com.luckyzyx.luckytool.utils.ModulePrefs

object StatusBarNotify : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.android.systemui") {
            //移除充电完成通知
            if (prefs(ModulePrefs).getBoolean("remove_charging_completed", false)) {
                loadHooker(RemoveChargingCompleted)
            }
            //移除状态栏开发者选项警告
            if (prefs(ModulePrefs).getBoolean("remove_statusbar_devmode", false)) {
                loadHooker(RemoveStatusBarDevMode)
            }
            //移除手电筒已开启通知
            if (prefs(ModulePrefs).getBoolean("remove_flashlight_open_notification", false)) {
                loadHooker(RemoveFlashlightOpenNotification)
            }
            //移除免打扰模式通知
            if (prefs(ModulePrefs).getBoolean("remove_do_not_disturb_mode_notification", false)) {
                loadHooker(RemoveDoNotDisturbModeNotification)
            }
            //移除通知勿扰通知
            if (prefs(ModulePrefs).getBoolean(
                    "remove_notifications_for_mute_notifications", false
                )
            ) {
                loadHooker(RemoveNotificationForMuteNotifications)
            }
            //移除GT模式通知
            if (prefs(ModulePrefs).getBoolean("remove_gt_mode_notification", false)) {
                loadHooker(RemoveGTModeNotification)
            }
        }
        if (packageName == "com.oplus.battery") {
            //移除电池通知
            loadHooker(RemoveBatteryNotify)
        }
        if (packageName == "com.coloros.phonemanager") {
            //移除手机管家发现病毒风险通知
            if (prefs(ModulePrefs).getBoolean(
                    "remove_virus_risk_notification_in_phone_manager", false
                )
            ) {
                loadHooker(RemoveVirusRiskNotificationInPhoneManager)
            }
        }
    }
}