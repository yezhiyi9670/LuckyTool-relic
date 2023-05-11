package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.jumpHighPerformance
import com.luckyzyx.luckytool.utils.jumpRunningApp

@Suppress("DEPRECATION")
class ShortcutActivity : Activity() {
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
            when (getString("Shortcut", "null")) {
                "module_shortcut_status_lsposed" -> {
                    ShellUtils.execCommand(
                        "am start 'intent:#Intent;action=android.intent.action.MAIN;category=org.lsposed.manager.LAUNCH_MANAGER;launchFlags=0x80000;component=com.android.shell/.BugreportWarningActivity;end'",
                        true
                    )
                }

                "module_shortcut_status_oplusgames" -> ShellUtils.execCommand(
                    "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity",
                    true
                )

                "module_shortcut_status_chargingtest" -> jumpBatteryInfo(this@ShortcutActivity)
                "module_shortcut_status_processmanager" -> jumpRunningApp(this@ShortcutActivity)
                "module_shortcut_status_performance" -> jumpHighPerformance(this@ShortcutActivity)
            }
        }
        finish()
    }
}