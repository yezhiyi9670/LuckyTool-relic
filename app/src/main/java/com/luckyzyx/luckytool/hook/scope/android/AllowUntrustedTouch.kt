package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object AllowUntrustedTouch : YukiBaseHooker() {
    override fun onHook() {
        if (SDK < A12) return
        val isEnable = prefs(ModulePrefs).getBoolean("allow_untrusted_touch", false)
        //Source UntrustedTouchController
        findClass("com.android.server.input.UntrustedTouchController").hook {
            injectMember {
                method { name = "isOplusTrustedApp" }
                if (isEnable) replaceToTrue()
            }
        }
        //Source WindowStateExtImpl
        findClass("com.android.server.wm.WindowStateExtImpl").hook {
            injectMember {
                method { name = "isOplusTrustedWindow" }
                if (isEnable) replaceToTrue()
            }
        }
        if (SDK >= A14) return
        //Source InputManager
        findClass("android.hardware.input.InputManager").hook {
            injectMember {
                method { name = "getBlockUntrustedTouchesMode" }
                if (isEnable) replaceTo(0)
            }
        }
    }
}