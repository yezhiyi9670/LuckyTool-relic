package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveFullScreenGestureSideSlideBar
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveFullScreenGestureSideSliderBackground
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class HookGestureRelated : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //移除全面屏手势侧滑条
            if (prefs(XposedPrefs).getBoolean("remove_full_screen_gesture_side_slidebar", false)) {
                loadHooker(RemoveFullScreenGestureSideSlideBar())
            }
            //移除全面屏手势侧滑条背景
            if (prefs(XposedPrefs).getBoolean("remove_full_screen_gesture_side_slider_background", false)) {
                loadHooker(RemoveFullScreenGestureSideSliderBackground())
            }
        }
    }
}