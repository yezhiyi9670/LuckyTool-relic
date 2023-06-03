package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveChargingCompleted
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveDoNotDisturbModeNotification
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveFlashlightOpenNotification
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveGTModeNotification
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveNotificationForMuteNotifications
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveStatusBarDevMode
import com.luckyzyx.luckytool.utils.ModulePrefs

object StatusBarNotify : YukiBaseHooker() {
    override fun onHook() {
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
}