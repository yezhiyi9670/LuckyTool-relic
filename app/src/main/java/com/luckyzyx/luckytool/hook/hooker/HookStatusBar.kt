package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.DoubleClickLockScreen
import com.luckyzyx.luckytool.hook.scope.systemui.HookSystemUIFeature
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //HookSystemUIFeature
        loadHooker(HookSystemUIFeature)

        //双击状态栏锁屏
        if (prefs(ModulePrefs).getBoolean("statusbar_double_click_lock_screen", false)) {
            loadHooker(DoubleClickLockScreen)
        }
    }
}