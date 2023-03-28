package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.FingerPrintIcon

object HookFingerPrintRelated : YukiBaseHooker() {
    override fun onHook() {
        //指纹图标
        loadHooker(FingerPrintIcon)
    }
}