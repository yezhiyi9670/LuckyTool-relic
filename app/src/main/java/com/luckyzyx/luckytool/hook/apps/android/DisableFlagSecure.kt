package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class DisableFlagSecure : YukiBaseHooker() {
    override fun onHook() {
        //SOurce WindowState
        var isEnable = prefs(XposedPrefs).getBoolean("disable_flag_secure", false)
        dataChannel.wait<Boolean>(key = "disable_flag_secure") { isEnable = it }
        findClass("com.android.server.wm.WindowState").hook {
            injectMember {
                method {
                    name = "isSecureLocked"
                }
                if (isEnable) replaceToFalse()
            }
        }
    }
}