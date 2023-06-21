package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.oplusmms.HookMMSFeatureOption
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object HookOplusMMS : YukiBaseHooker() {
    override fun onHook() {
        //HookMMSFeatureOption
        if (SDK >= A13) loadHooker(HookMMSFeatureOption)
    }
}