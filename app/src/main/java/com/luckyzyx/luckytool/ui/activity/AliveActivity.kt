package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.highcapable.yukihookapi.hook.factory.modulePrefs
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.utils.data.SettingsPrefs
import com.luckyzyx.luckytool.utils.data.XposedPrefs
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.getInt

@Obfuscate
@Suppress("DEPRECATION")
class AliveActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** 设置透明窗口 */
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        //FPS
        if (modulePrefs(SettingsPrefs).getBoolean("fps_autostart", false)) {
            val fps = getInt(SettingsPrefs, "current_fps", -1)
            if (fps == -1) return
            ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $fps", true)
        }
        //触控采样率
        if (modulePrefs(XposedPrefs).getBoolean("increase_touch_sampling_rate", false)) {
            ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true)
        }

        finishAndRemoveTask()
    }
}