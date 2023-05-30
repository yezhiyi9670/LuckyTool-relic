package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveAodMusicWhitelist : YukiBaseHooker() {
    override fun onHook() {
        //Source AodMediaDataListener
        findClass("com.oplusos.systemui.aod.mediapanel.AodMediaDataListener\$Companion").hook {
            injectMember {
                method { name = "isAodMediaSupport" }
                replaceToTrue()
            }
            injectMember {
                method { name = "isAodMediaSupportWithoutFeature" }
                replaceToTrue()
            }
        }
    }
}