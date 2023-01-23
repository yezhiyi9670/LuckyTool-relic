package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.exsystemservice.RemoveWarningDialogThatAppRunsOnDesktop
import com.luckyzyx.luckytool.hook.scope.systemui.DisableDuplicateFloatingWindow
import com.luckyzyx.luckytool.hook.scope.systemui.DisableHeadphoneHighVolumeWarning
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveLowBatteryDialogWarning
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveUSBConnectDialog
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object HookDialogRelated : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.android.systemui") {
            //禁用复制悬浮窗
            if (prefs(XposedPrefs).getBoolean("disable_duplicate_floating_window", false)) {
                if (SDK >= A13) loadHooker(DisableDuplicateFloatingWindow)
            }
            //禁用耳机高音量警告
            if (prefs(XposedPrefs).getBoolean("disable_headphone_high_volume_warning", false)) {
                loadHooker(DisableHeadphoneHighVolumeWarning)
            }
            //移除低电量对话框警告
            if (prefs(XposedPrefs).getBoolean("remove_low_battery_dialog_warning", false)) {
                loadHooker(RemoveLowBatteryDialogWarning)
            }
            //移除USB连接对话框
            if (prefs(XposedPrefs).getBoolean("remove_usb_connect_dialog", false)) {
                loadHooker(RemoveUSBConnectDialog)
            }
        }

        if (packageName == "com.oplus.exsystemservice") {
            //移除应用运行在桌面上警告对话框
            if (prefs(XposedPrefs).getBoolean("remove_warning_dialog_that_app_runs_on_desktop", false)) {
                loadHooker(RemoveWarningDialogThatAppRunsOnDesktop)
            }
        }
    }
}