package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object AllowUntrustedTouch : YukiBaseHooker() {
    override fun onHook() {
        findClass("android.hardware.input.InputManager").hook {
            injectMember {
                method {
                    name = "getBlockUntrustedTouchesMode"
                    paramCount = 1
                }
                if (prefs(XposedPrefs).getBoolean("allow_untrusted_touch", false)) replaceTo(0)
            }
        }
    }
}