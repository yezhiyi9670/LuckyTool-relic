package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object RemoveStatusBarTopNotification : YukiBaseHooker() {
    override fun onHook() {
        //Source AlertWindowNotification
        findClass("com.android.server.wm.AlertWindowNotification").hook {
            injectMember {
                method {
                    name = "onPostNotification"
                }
                if (prefs(ModulePrefs).getBoolean("remove_statusbar_top_notification", false)) intercept()
            }
        }
    }
}