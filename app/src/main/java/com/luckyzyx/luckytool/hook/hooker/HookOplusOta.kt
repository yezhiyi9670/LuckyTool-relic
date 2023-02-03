package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.oplusota.UnlockLocalUpgrade
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookOplusOta : YukiBaseHooker() {
    override fun onHook() {
        //解锁本地安装
        if (prefs(ModulePrefs).getBoolean("unlock_local_upgrade", false)) {
            loadHooker(UnlockLocalUpgrade)
        }
    }
}