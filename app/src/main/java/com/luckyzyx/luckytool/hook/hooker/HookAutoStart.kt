package com.luckyzyx.luckytool.hook.hooker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.drake.net.utils.scope
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity
import com.luckyzyx.luckytool.utils.data.callFunc
import com.luckyzyx.luckytool.utils.data.setRefresh
import com.luckyzyx.luckytool.utils.tools.SettingsPrefs
import kotlinx.coroutines.delay

object HookAutoStart : YukiBaseHooker() {
    override fun onHook() {
        val fpsList = arrayOf("30.0", "60.0", "90.0", "120.0")
        var callMode = prefs(SettingsPrefs).getString("switch_autostart_function_caller", "0")
        dataChannel.wait<String>("switch_autostart_function_caller") { callMode = it }

        var fpsAutoStart = prefs(SettingsPrefs).getBoolean("fps_autostart", false)
        dataChannel.wait<Boolean>("fps_autostart") { fpsAutoStart = it }
        var fpsMode = prefs(SettingsPrefs).getInt("fps_mode", 1)
        dataChannel.wait<Int>("fps_mode") { fpsMode = it }
        var currentFps = prefs(SettingsPrefs).getInt("current_fps", -1)
        dataChannel.wait<Int>("current_fps") { currentFps = it }
        var touchSamplingRate = prefs(SettingsPrefs).getBoolean("touch_sampling_rate", false)
        dataChannel.wait<Boolean>("touch_sampling_rate") { touchSamplingRate = it }
        var highBrightness = prefs(SettingsPrefs).getBoolean("high_brightness_mode", false)
        dataChannel.wait<Boolean>("high_brightness_mode") { highBrightness = it }
        var globalDC = prefs(SettingsPrefs).getBoolean("global_dc_mode", false)
        dataChannel.wait<Boolean>("global_dc_mode") { globalDC = it }
        var highPerformance = prefs(SettingsPrefs).getBoolean("high_performance_mode", false)
        dataChannel.wait<Boolean>("high_performance_mode") { highPerformance = it }

        onAppLifecycle {
            //监听锁屏解锁
            registerReceiver(Intent.ACTION_USER_PRESENT) { context, _ ->
                scope {
                    delay(200)
                    if (fpsAutoStart && (fpsMode == 1) && (currentFps != -1)) {
                        setRefresh(context, fpsList[currentFps], fpsList[currentFps])
                    }
                    val bundle = Bundle().apply {
                        putBoolean("fps", fpsAutoStart && fpsMode == 2)
                        putBoolean("touchSamplingRate", touchSamplingRate)
                        putBoolean("highBrightness", highBrightness)
                        putBoolean("globalDC", globalDC)
                        putBoolean("highPerformance", highPerformance)
                    }
                    when (callMode) {
                        "0" -> {
                            for (key in bundle.keySet()) {
                                if (bundle.getBoolean(key)) {
                                    context.callModule(bundle)
                                    break
                                }
                            }
                        }
                        "1" -> context.callFunc(bundle)
                    }
                }
            }
        }
    }

    private fun Context.callModule(bundle: Bundle) {
        Intent(Intent.ACTION_VIEW).apply {
            setClassName(BuildConfig.APPLICATION_ID, AliveActivity::class.java.name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            putExtras(bundle)
            startActivity(this)
        }
    }
}