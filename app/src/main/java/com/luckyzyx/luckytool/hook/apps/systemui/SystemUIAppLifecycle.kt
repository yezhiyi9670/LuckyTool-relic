package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.Intent
import com.drake.net.utils.scope
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.ui.activity.AliveActivity
import kotlinx.coroutines.delay

class SystemUIAppLifecycle : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            onAppLifecycle {
                //监听锁屏解锁
                registerReceiver(Intent.ACTION_USER_PRESENT) { context, _ ->
                    scope {
                        delay(3000)
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