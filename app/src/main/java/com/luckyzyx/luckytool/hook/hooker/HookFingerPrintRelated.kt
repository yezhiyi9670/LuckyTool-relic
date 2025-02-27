package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.FingerPrintIconAnim

object HookFingerPrintRelated : YukiBaseHooker() {
    override fun onHook() {
        //指纹图标
        loadHooker(FingerPrintIconAnim)
    }
}