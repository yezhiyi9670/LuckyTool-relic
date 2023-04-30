package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object AllowUntrustedTouch : YukiBaseHooker() {
    override fun onHook() {
        if (SDK < A12) return
        val isEnable = prefs(ModulePrefs).getBoolean("allow_untrusted_touch", false)
        //Source InputManager
        findClass("android.hardware.input.InputManager").hook {
            injectMember {
                method {
                    name = "getBlockUntrustedTouchesMode"
                    paramCount = 1
                }
                if (isEnable) replaceTo(0)
            }
        }
    }
}