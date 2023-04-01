package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object AllowUntrustedTouch : YukiBaseHooker() {
    override fun onHook() {
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