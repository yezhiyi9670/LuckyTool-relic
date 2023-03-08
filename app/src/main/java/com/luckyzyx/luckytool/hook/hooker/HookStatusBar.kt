package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.DoubleClickLockScreen
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarCarriersUseUserTypeface
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //双击状态栏锁屏
        if (prefs(ModulePrefs).getBoolean("statusbar_double_click_lock_screen", false)) {
            loadHooker(DoubleClickLockScreen)
        }

        //运营商使用用户字体
        if (prefs(ModulePrefs).getBoolean("statusbar_carriers_use_user_typeface", false)) {
            loadHooker(StatusBarCarriersUseUserTypeface)
        }
    }
}