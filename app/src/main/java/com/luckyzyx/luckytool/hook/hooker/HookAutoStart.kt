package com.luckyzyx.luckytool.hook.hooker

import android.content.Intent
import com.drake.net.utils.scope
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity
import com.luckyzyx.luckytool.utils.data.setRefresh
import com.luckyzyx.luckytool.utils.tools.SettingsPrefs
import kotlinx.coroutines.delay

object HookAutoStart : YukiBaseHooker() {
    override fun onHook() {
        val fpsList = arrayOf("30.0", "60.0", "90.0", "120.0")
        var fpsAutoStart = prefs(SettingsPrefs).getBoolean("fps_autostart", false)
        dataChannel.wait<Boolean>("fps_autostart") { fpsAutoStart = it }
        var fpsMode = prefs(SettingsPrefs).getInt("fps_mode", 1)
        dataChannel.wait<Int>("fps_mode") { fpsMode = it }
        var currentFps = prefs(SettingsPrefs).getInt("current_fps", -1)
        dataChannel.wait<Int>("current_fps") { currentFps = it }
        val touchSamplingRate = prefs(SettingsPrefs).getBoolean("touch_sampling_rate", false)
        val highBrightness = prefs(SettingsPrefs).getBoolean("high_brightness_mode", false)
        val globalDC = prefs(SettingsPrefs).getBoolean("global_dc_mode", false)
        val highPerformance = prefs(SettingsPrefs).getBoolean("high_performance_mode", false)
        onAppLifecycle {
            //监听锁屏解锁
            registerReceiver(Intent.ACTION_USER_PRESENT) { context, _ ->
                scope {
                    delay(200)
                    if (fpsAutoStart && (fpsMode == 1) && (currentFps != -1)) {
                        setRefresh(context, fpsList[currentFps], fpsList[currentFps])
                    }
                    if (!((fpsAutoStart && fpsMode == 2) || touchSamplingRate || highBrightness || globalDC || highPerformance)) return@scope
                    Intent(Intent.ACTION_VIEW).apply {
                        setClassName(BuildConfig.APPLICATION_ID, AliveActivity::class.java.name)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        putExtra("fps", fpsAutoStart && fpsMode == 2)
                        putExtra("touchSamplingRate", touchSamplingRate)
                        putExtra("highBrightness", highBrightness)
                        putExtra("globalDC", globalDC)
                        putExtra("highPerformance", highPerformance)
                        context.startActivity(this)
                    }
                }
            }
        }
    }
}