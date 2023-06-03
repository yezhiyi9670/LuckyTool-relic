package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.UnlockStartupLimit
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimitC12
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookSafeCenter : YukiBaseHooker() {
    override fun onHook() {
        //移除自启数量限制
        if (prefs(ModulePrefs).getBoolean("unlock_startup_limit", false)) {
            if (SDK >= A13 && packageName == "com.oplus.battery") {
                //电池
                loadHooker(UnlockStartupLimit)
            } else {
                //安全中心
                loadHooker(UnlockStartupLimitC12)
            }
        }
    }
}