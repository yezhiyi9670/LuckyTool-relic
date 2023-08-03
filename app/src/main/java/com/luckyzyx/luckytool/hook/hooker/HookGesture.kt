package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.gesture.CustomAonGestureScrollPageWhitelist
import com.luckyzyx.luckytool.hook.scope.gesture.EnableAonGestures
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookGesture : YukiBaseHooker() {
    override fun onHook() {
        //启用隔空手势
        if (prefs(ModulePrefs).getBoolean("force_enable_aon_gestures", false)) {
            loadHooker(EnableAonGestures)
        }

        //自定义滑动页面白名单
        //自定义视频手势白名单
        loadHooker(CustomAonGestureScrollPageWhitelist)
    }
}