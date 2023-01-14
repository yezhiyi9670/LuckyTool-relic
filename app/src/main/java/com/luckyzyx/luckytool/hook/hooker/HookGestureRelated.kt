package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.FullScreenGestureSideSlideBar
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveRotateScreenButton
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object HookGestureRelated : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //全面屏手势侧滑条
            loadHooker(FullScreenGestureSideSlideBar)

            //移除旋转屏幕按钮
            if (prefs(XposedPrefs).getBoolean("remove_rotate_screen_button", false)) {
                loadHooker(RemoveRotateScreenButton)
            }
        }
    }
}