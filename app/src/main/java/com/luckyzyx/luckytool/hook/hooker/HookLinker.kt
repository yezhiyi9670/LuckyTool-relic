package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.opluslinker.ForceDisplayCommunicationSharing
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookLinker : YukiBaseHooker() {
    override fun onHook() {
        //强制显示通信共享
        if (prefs(ModulePrefs).getBoolean("force_display_communication_sharing", false)) {
            if (SDK >= A13) loadHooker(ForceDisplayCommunicationSharing)
        }
    }
}