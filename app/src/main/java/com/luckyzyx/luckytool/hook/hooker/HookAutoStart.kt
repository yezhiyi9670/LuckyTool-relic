package com.luckyzyx.luckytool.hook.hooker

import android.content.Intent
import com.drake.net.utils.scope
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity
import com.luckyzyx.luckytool.utils.data.setRefresh
import com.luckyzyx.luckytool.utils.tools.ModulePrefs
import kotlinx.coroutines.delay

object HookAutoStart : YukiBaseHooker() {
    override fun onHook() {
        val fpsList = arrayOf("30.0", "60.0", "90.0", "120.0")
        var fpsAutoStart = prefs(ModulePrefs).getBoolean("fps_autostart", false)
        dataChannel.wait<Boolean>(key = "fps_autostart") { fpsAutoStart = it }
        var fpsMode = prefs(ModulePrefs).getInt("fps_mode", 1)
        dataChannel.wait<Int>(key = "fps_mode") { fpsMode = it }
        var currentFps = prefs(ModulePrefs).getInt("current_fps", -1)
        dataChannel.wait<Int>(key = "current_fps") { currentFps = it }
        onAppLifecycle {
            //监听锁屏解锁
            registerReceiver(Intent.ACTION_USER_PRESENT) { context, _ ->
                scope {
                    delay(100)

                    if (fpsAutoStart && (fpsMode == 1) && (currentFps != -1)) {
                        setRefresh(context, fpsList[currentFps], fpsList[currentFps])
                    }

                    Intent(Intent.ACTION_VIEW).apply {
                        setClassName(BuildConfig.APPLICATION_ID, AliveActivity::class.java.name)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        putExtra("fps", fpsAutoStart && fpsMode == 2)
                        context.startActivity(this)
                    }
                }
            }
        }
    }
}