package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveFullScreenGestureSideSlideBar

class HookGestureRelated : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除全面屏手势侧滑条
            loadHooker(RemoveFullScreenGestureSideSlideBar())
        }
    }
}