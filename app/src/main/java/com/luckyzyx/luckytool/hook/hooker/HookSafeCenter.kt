package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimitOld
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookSafeCenter : YukiBaseHooker() {
    override fun onHook() {
        //移除自启数量限制
        if (SDK < A13 && prefs(ModulePrefs).getBoolean("unlock_startup_limit", false)) {
            loadHooker(UnlockStartupLimitOld)
        }
    }
}