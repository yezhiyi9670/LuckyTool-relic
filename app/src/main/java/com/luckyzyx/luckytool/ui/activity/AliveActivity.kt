package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.utils.data.*
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.getBoolean
import com.luckyzyx.luckytool.utils.tools.getInt

@Obfuscate
@Suppress("DEPRECATION")
class AliveActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** 设置透明窗口 */
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        val context = this
        intent.extras.apply {
            if (this == null) return@apply
            //自启功能相关
            if(this.getBoolean("SelfStart")){
                if (getBoolean(SettingsPrefs,"fps_autostart", false) && (getInt(SettingsPrefs,"fps_mode",1) == 2)){
                val fps = getInt(SettingsPrefs, "current_fps", -1)
                if (fps != -1) ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $fps", true,true).result.apply {
                    if (this == 1) toast("force fps error!")
                }
                }
                if (getBoolean(XposedPrefs,"increase_touch_sampling_rate", false)){
                    ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true,true).result.apply {
                        if (this == 1) toast("touch sampling rate error!")
                    }
                }
            }
            //快捷方式相关
            when(this.getString("Shortcut")){
                "lsposed" -> ShellUtils.execCommand("am start 'intent:#Intent;action=android.intent.action.MAIN;category=org.lsposed.manager.LAUNCH_MANAGER;launchFlags=0x80000;component=com.android.shell/.BugreportWarningActivity;end'",true)
                "oplusGames" -> ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true)
                "processManager" -> jumpRunningApp(context)
                "chargingTest" -> jumpBatteryInfo(context)
            }
        }
        finishAndRemoveTask()
    }
}