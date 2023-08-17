package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.permissioncontroller.UnlockDefaultDesktopLimit
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookPermissionController : YukiBaseHooker() {
    override fun onHook() {
        //解锁默认桌面限制
        if (prefs(ModulePrefs).getBoolean("unlock_default_desktop_limit", false)) {
            loadHooker(UnlockDefaultDesktopLimit)
        }
    }
}