package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveFingerPrintIcon

object HookFingerPrintRelated : YukiBaseHooker() {
    override fun onHook() {
        //移除锁屏指纹图标
        loadHooker(RemoveFingerPrintIcon)
    }
}