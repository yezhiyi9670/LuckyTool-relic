package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.themestore.UnlockThemeStoreVip
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookThemeStore : YukiBaseHooker() {
    override fun onHook() {
        //解锁主题商店VIP
        if (prefs(XposedPrefs).getBoolean("unlock_themestore_vip",false)) loadHooker(
            UnlockThemeStoreVip()
        )
    }
}