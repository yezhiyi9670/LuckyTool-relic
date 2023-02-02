package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.utils.data.*
import com.luckyzyx.luckytool.utils.tools.*

@Suppress("DEPRECATION")
@Obfuscate
class AliveActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** 设置透明窗口 */
//        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        window?.statusBarColor = getColor(R.color.transparent)
//        window?.navigationBarColor = getColor(R.color.transparent)
        intent.extras?.apply {
            //自启功能相关
            if(getBoolean("fps")) {
                val fpsCur = getInt(SettingsPrefs, "current_fps", -1)
                if (fpsCur != -1) ShellUtils.execCommand("service call SurfaceFlinger 1035 i32 $fpsCur", true,true).result.apply {
                    if (this == 1) toast("force fps error!")
                }
            }
            //触控采样率相关
            if (getBoolean("touch")) {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true,true).result.apply {
                    if (this == 1) toast("touch sampling rate error!")
                }
            }
            //快捷方式相关
            when(getString("Shortcut")) {
                "lsposed" -> ShellUtils.execCommand("am start 'intent:#Intent;action=android.intent.action.MAIN;category=org.lsposed.manager.LAUNCH_MANAGER;launchFlags=0x80000;component=com.android.shell/.BugreportWarningActivity;end'",true)
                "oplusGames" -> ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true)
                "processManager" -> jumpRunningApp(this@AliveActivity)
                "chargingTest" -> jumpBatteryInfo(this@AliveActivity)
            }
        }
        finish()
    }
}