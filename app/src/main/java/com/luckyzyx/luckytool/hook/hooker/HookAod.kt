package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object HookAod : YukiBaseHooker() {
    override fun onHook() {
        //Source AodSettingsValueProxy
        findClass("com.oplus.aod.proxy.AodSettingsValueProxy").hook {
            injectMember {
                method { name = "getAodSceneMusicSupport" }
                replaceToTrue()
            }
            injectMember {
                method { name = "getAodSceneMusicSwitchEnable" }
//                    replaceTo(1)
            }
        }
    }
}