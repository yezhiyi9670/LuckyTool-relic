package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.uiengine.RemoveAodNotificationWhitelist
import com.luckyzyx.luckytool.hook.scope.uiengine.SetAodStyleMode
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookUIEngine : YukiBaseHooker() {
    override fun onHook() {
        if (SDK != A13) return

        //移除通知图标白名单
        if (prefs(ModulePrefs).getBoolean("remove_aod_notification_icon_whitelist", false)) {
            loadHooker(RemoveAodNotificationWhitelist)
        }

        //设置息屏样式模式
        loadHooker(SetAodStyleMode)
    }
}