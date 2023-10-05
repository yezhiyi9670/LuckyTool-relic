package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.AllowLongPressNotificationModifiable
import com.luckyzyx.luckytool.utils.ModulePrefs

object StatusBarNotifiyLimit : YukiBaseHooker() {
    override fun onHook() {
        //允许长按通知可修改
        if (prefs(ModulePrefs).getBoolean("allow_long_press_notification_modifiable", false)) {
            loadHooker(AllowLongPressNotificationModifiable)
        }
    }
}