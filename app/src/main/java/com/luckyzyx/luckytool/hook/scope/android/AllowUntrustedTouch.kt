package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object AllowUntrustedTouch : YukiBaseHooker() {
    override fun onHook() {
        if (SDK < A12) return
        val isEnable = prefs(ModulePrefs).getBoolean("allow_untrusted_touch", false)

        //Source UntrustedTouchController
        "com.android.server.input.UntrustedTouchController".toClass().apply {
            method { name = "isOplusTrustedApp" }.hook {
                if (isEnable) replaceToTrue()
            }
        }
        //Source WindowStateExtImpl
        "com.android.server.wm.WindowStateExtImpl".toClass().apply {
            method { name = "isOplusTrustedWindow" }.hook {
                if (isEnable) replaceToTrue()
            }
        }
        if (SDK >= A14) return
        //Source InputManager
        "android.hardware.input.InputManager".toClass().apply {
            method { name = "getBlockUntrustedTouchesMode" }.hook {
                if (isEnable) replaceTo(0)
            }
        }
    }
}