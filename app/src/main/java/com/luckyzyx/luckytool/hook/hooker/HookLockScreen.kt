package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.*
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.ModulePrefs


object HookLockScreen : YukiBaseHooker() {
    override fun onHook() {
        //移除锁屏时钟红1
        if (prefs(ModulePrefs).getBoolean("remove_lock_screen_redone", false)) {
            loadHooker(RemoveLockScreenRedOne)
        }

        //锁屏组件
        loadHooker(LockScreenComponent)

        //锁屏充电组件
        loadHooker(LockScreenChargingComponent)

        //移除锁屏下方按钮
        loadHooker(RemoveLockScreenBottomButton)

        //移除SOS紧急联络按钮
        if (prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_sos_button", false)) {
            if (SDK >= A13) loadHooker(RemoveLockScreenBottomSOSButton)
        }

        //移除锁屏顶部图标
        if (prefs(ModulePrefs).getBoolean("remove_top_lock_screen_icon", false)) {
            loadHooker(RemoveTopLockScreenIcon)
        }

        //移除锁屏关闭通知按钮
        if (prefs(ModulePrefs).getBoolean("remove_lock_screen_close_notification_button", false)) {
            loadHooker(RemoveLockScreenCloseNotificationButton)
        }
    }
}