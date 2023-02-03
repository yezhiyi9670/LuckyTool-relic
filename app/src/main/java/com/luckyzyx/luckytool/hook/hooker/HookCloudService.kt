package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.heytapcloud.RemoveNetworkRestriction
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookCloudService : YukiBaseHooker() {
    override fun onHook() {
        //移除网络限制
        if (prefs(ModulePrefs).getBoolean("remove_network_limit",false)) {
            loadHooker(RemoveNetworkRestriction)
        }
    }
}