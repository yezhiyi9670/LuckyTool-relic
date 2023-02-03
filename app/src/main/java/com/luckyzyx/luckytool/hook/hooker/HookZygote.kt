package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.android.DisableFlagSecureZygote
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookZygote : YukiBaseHooker() {
    override fun onHook() {
        //禁用FLAG_SECURE
        if (prefs(ModulePrefs).getBoolean("disable_flag_secure",false)){
            loadHooker(DisableFlagSecureZygote)
        }
    }
}