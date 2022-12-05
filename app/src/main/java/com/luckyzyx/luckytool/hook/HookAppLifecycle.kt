package com.luckyzyx.luckytool.hook

import android.content.Intent
import com.drake.net.utils.scope
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity
import com.luckyzyx.luckytool.utils.data.SettingsPrefs
import com.luckyzyx.luckytool.utils.data.setRefresh
import kotlinx.coroutines.delay

class HookAppLifecycle : YukiBaseHooker() {
    override fun onHook() {
        val fpsList = arrayOf("30.0", "60.0", "90.0", "120.0")
        var fpsAutoStart = prefs(SettingsPrefs).getBoolean("fps_autostart", false)
        var fpsMode = prefs(SettingsPrefs).getInt("fps_mode", 1)
        var currentFps = prefs(SettingsPrefs).getInt("current_fps", -1)
        dataChannel.wait<Boolean>(key = "fps_autostart") { fpsAutoStart = it }
        dataChannel.wait<Int>(key = "fps_mode") { fpsMode = it }
        dataChannel.wait<Int>(key = "current_fps") { currentFps = it }
        loadApp("com.android.systemui") {
            onAppLifecycle {
                //监听锁屏解锁
                registerReceiver(Intent.ACTION_USER_PRESENT) { context, _ ->
                    scope {
                        delay(1000)

                        if (fpsAutoStart && (fpsMode == 1) && (currentFps != -1)) {
                            setRefresh(context, fpsList[currentFps], fpsList[currentFps])
                        }

                        Intent(Intent.ACTION_VIEW).apply {
                            setClassName(BuildConfig.APPLICATION_ID, AliveActivity::class.java.name)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            putExtra("SelfStart", true)
                            context.startActivity(this)
                        }
                    }
                }
            }
        }
    }
}