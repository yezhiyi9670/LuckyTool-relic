package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object AppSplashScreen : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("disable_splash_screen", false)
        //Source StartingSurfaceController
        if (SDK < A13) return
        findClass("com.android.server.wm.StartingSurfaceController").hook {
            injectMember {
                method { name = "showStartingWindow";paramCount = 5 }
                if (isEnable) intercept()
            }
        }
    }
}