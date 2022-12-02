package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.ComponentName
import android.content.Intent
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity

class SystemUIAppLifecycle : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            onAppLifecycle {
                registerReceiver(Intent.ACTION_BOOT_COMPLETED) { context, _ ->
                    Intent(Intent.ACTION_VIEW).apply {
                        component = ComponentName(
                            BuildConfig.APPLICATION_ID,
                            AliveActivity::class.java.name
                        )
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(this)
                    }
                }
            }
        }
    }
}