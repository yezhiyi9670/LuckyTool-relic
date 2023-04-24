package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.themestore.UnlockThemeStoreVip
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookThemeStore : YukiBaseHooker() {
    override fun onHook() {
        //解锁主题商店VIP
        if (prefs(ModulePrefs).getBoolean("unlock_themestore_vip",false)) {
            loadHooker(UnlockThemeStoreVip)
        }
    }
}