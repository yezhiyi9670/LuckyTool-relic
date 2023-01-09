package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object AppSplashScreen : YukiBaseHooker() {
    override fun onHook() {
        //Source StartingSurfaceController
        if (SDK < A13) return
        findClass("com.android.server.wm.StartingSurfaceController").hook {
            injectMember {
                method {
                    name = "showStartingWindow"
                    paramCount = 5
                }
                if (prefs(XposedPrefs).getBoolean("disable_splash_screen", false)) intercept()
            }
        }
    }
}