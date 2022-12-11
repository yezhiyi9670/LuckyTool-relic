package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class RemoveStatusBarTopNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source AlertWindowNotification
        findClass("com.android.server.wm.AlertWindowNotification").hook {
            injectMember {
                method {
                    name = "onPostNotification"
                }
                var isEnable = prefs(XposedPrefs).getBoolean("remove_statusbar_top_notification", false)
                dataChannel.wait<Boolean>(key = "remove_statusbar_top_notification") { isEnable = it }
                if (isEnable) intercept()
            }
        }
    }
}