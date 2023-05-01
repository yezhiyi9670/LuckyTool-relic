package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.notificationmanager.RemoveNotificationManagerLimit
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookNotificationManager : YukiBaseHooker() {
    override fun onHook() {
        //移除通知管理限制
        if (prefs(ModulePrefs).getBoolean("remove_notification_manager_limit", false)) {
            loadHooker(RemoveNotificationManagerLimit)
        }
    }
}