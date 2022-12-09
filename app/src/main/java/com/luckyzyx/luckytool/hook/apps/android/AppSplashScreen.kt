package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class AppSplashScreen : YukiBaseHooker() {
    override fun onHook() {
        //Source StartingSurfaceController
        if (SDK < A13) return
        var isEnable = prefs(XposedPrefs).getBoolean("disable_splash_screen", false)
        dataChannel.wait<Boolean>(key = "disable_splash_screen") { isEnable = it }
        findClass("com.android.server.wm.StartingSurfaceController").hook {
            injectMember {
                method {
                    name = "showStartingWindow"
                    paramCount = 5
                }
                if (isEnable) intercept()
            }
        }
    }
}