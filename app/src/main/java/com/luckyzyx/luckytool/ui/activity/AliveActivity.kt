package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.data.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.data.jumpRunningApp
import com.luckyzyx.luckytool.utils.data.toast
import com.luckyzyx.luckytool.utils.tools.SettingsPrefs
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.getBoolean
import com.luckyzyx.luckytool.utils.tools.getInt

@Suppress("DEPRECATION")
@Obfuscate
class AliveActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** 设置透明窗口 */
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window?.statusBarColor = getColor(R.color.transparent)
        window?.navigationBarColor = getColor(R.color.transparent)
        intent.extras?.apply {
            //自启功能相关
            if (getBoolean("fps")) {
                val fpsCur = getInt(SettingsPrefs, "current_fps", -1)
                if (fpsCur != -1) ShellUtils.execCommand(
                    "service call SurfaceFlinger 1035 i32 $fpsCur",
                    true,
                    true
                ).apply {
                    if (result == 1) toast("force fps error!")
                }
            }
            //触控采样率相关
            if (getBoolean(SettingsPrefs, "touch_sampling_rate", false)) {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true, true)
                    .apply {
                        if (result == 1) toast("touch sampling rate error!")
                    }
            }
            //高亮度模式
            if (getBoolean(SettingsPrefs, "high_brightness_mode", false)) {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 1", true, true).apply {
                    if (result == 1) toast("high brightness mode error!")
                }
            }
            //全局DC模式
            if (getBoolean(SettingsPrefs, "global_dc_mode", false)) {
                var oppoError = false
                var oplusError = false
                ShellUtils.execCommand("echo > /sys/kernel/oppo_display/dimlayer_hbm 1", true)
                    .apply {
                        if (result == 1) oppoError = true
                    }
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 1", true)
                    .apply {
                        if (result == 1) oplusError = true
                    }
                if (oppoError && oplusError) toast("global dc mode error!")
            }
            //快捷方式相关
            when (getString("Shortcut")) {
                "lsposed" -> ShellUtils.execCommand(
                    "am start 'intent:#Intent;action=android.intent.action.MAIN;category=org.lsposed.manager.LAUNCH_MANAGER;launchFlags=0x80000;component=com.android.shell/.BugreportWarningActivity;end'",
                    true
                )
                "oplusGames" -> ShellUtils.execCommand(
                    "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity",
                    true
                )
                "processManager" -> jumpRunningApp(this@AliveActivity)
                "chargingTest" -> jumpBatteryInfo(this@AliveActivity)
            }
        }
        finish()
    }
}