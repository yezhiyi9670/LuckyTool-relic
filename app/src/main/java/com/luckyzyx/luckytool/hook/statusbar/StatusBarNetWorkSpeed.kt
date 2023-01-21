package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.NetworkSpeed

object StatusBarNetWorkSpeed : YukiBaseHooker() {
    override fun onHook() {
        //状态栏网速
        loadHooker(NetworkSpeed)
    }
}