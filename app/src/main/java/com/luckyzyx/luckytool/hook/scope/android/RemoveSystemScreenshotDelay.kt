package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object RemoveSystemScreenshotDelay : YukiBaseHooker() {
    override fun onHook() {
        //Source PhoneWindowManager
        findClass("com.android.server.policy.PhoneWindowManager").hook {
            injectMember {
                method {
                    name = "getScreenshotChordLongPressDelay"
                    returnType = LongType
                }
                if (prefs(XposedPrefs).getBoolean("remove_system_screenshot_delay", false)) replaceTo(0L)
            }
        }
    }
}