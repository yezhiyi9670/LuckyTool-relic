package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.android.DisableFlagSecureZygote
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookZygote : YukiBaseHooker() {
    override fun onHook() {
        //禁用FLAG_SECURE
        if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)){
            loadHooker(DisableFlagSecureZygote())
        }
    }
}