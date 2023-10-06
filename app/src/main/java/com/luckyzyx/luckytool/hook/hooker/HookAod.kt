package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object HookAod : YukiBaseHooker() {
    override fun onHook() {
        //Source AodSettingsValueProxy
        "com.oplus.aod.proxy.AodSettingsValueProxy".toClass().apply {
            method { name = "getAodSceneMusicSupport" }.hook().replaceToTrue()
            method { name = "getAodSceneMusicSwitchEnable" }.hook().replaceTo(1)
        }
    }
}