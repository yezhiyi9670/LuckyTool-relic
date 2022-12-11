package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class RemoveSystemScreenshotDelay : YukiBaseHooker() {
    override fun onHook() {
        //Source PhoneWindowManager
        findClass("com.android.server.policy.PhoneWindowManager").hook {
            injectMember {
                method {
                    name = "getScreenshotChordLongPressDelay"
                    returnType = LongType
                }
                var isEnable = prefs(XposedPrefs).getBoolean("remove_system_screenshot_delay", false)
                dataChannel.wait<Boolean>(key = "remove_system_screenshot_delay") { isEnable = it }
                if (isEnable) replaceTo(0L)
            }
        }
    }
}