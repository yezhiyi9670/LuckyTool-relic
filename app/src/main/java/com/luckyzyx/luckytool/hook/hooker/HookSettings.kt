package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.settings.RemoveTopAccountDisplay
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookSettings : YukiBaseHooker() {
    override fun onHook() {
        //移除顶部账号显示
        if (prefs(ModulePrefs).getBoolean("remove_top_account_display", false)) {
            loadHooker(RemoveTopAccountDisplay)
        }
    }
}