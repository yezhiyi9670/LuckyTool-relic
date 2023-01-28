package com.luckyzyx.luckytool.hook.hooker

import android.content.Context
import android.content.Intent
import com.drake.net.utils.scope
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity
import com.luckyzyx.luckytool.utils.data.setRefresh
import com.luckyzyx.luckytool.utils.tools.SettingsPrefs
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import kotlinx.coroutines.delay

object HookAutoFps : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            val fpsList = arrayOf("30.0", "60.0", "90.0", "120.0")
            var fpsAutoStart = prefs(SettingsPrefs).getBoolean("fps_autostart", false)
            var fpsMode = prefs(SettingsPrefs).getInt("fps_mode", 1)
            var currentFps = prefs(SettingsPrefs).getInt("current_fps", -1)
            var touchRate = prefs(XposedPrefs).getBoolean("increase_touch_sampling_rate", false)
            dataChannel.wait<Boolean>(key = "fps_autostart") { fpsAutoStart = it }
            dataChannel.wait<Int>(key = "fps_mode") { fpsMode = it }
            dataChannel.wait<Int>(key = "current_fps") { currentFps = it }
            dataChannel.wait<Boolean>(key = "increase_touch_sampling_rate") { touchRate = it }
            onAppLifecycle {
                //监听锁屏解锁
                registerReceiver(Intent.ACTION_USER_PRESENT) { context, _ ->
                    scope {
                        delay(500)
                        if (fpsAutoStart || touchRate) {
                            if ((fpsMode == 1) && (currentFps != -1)) {
                                setRefresh(context, fpsList[currentFps], fpsList[currentFps])
                            }
                            callModule(context, fpsAutoStart, touchRate)
                        }
                    }
                }
            }
        }
    }

    private fun callModule(context: Context, fps: Boolean, touch: Boolean) {
        Intent(Intent.ACTION_VIEW).apply {
            setClassName(BuildConfig.APPLICATION_ID, AliveActivity::class.java.name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            putExtra("fps", fps)
            putExtra("touch", touch)
            context.startActivity(this)
        }
    }
}