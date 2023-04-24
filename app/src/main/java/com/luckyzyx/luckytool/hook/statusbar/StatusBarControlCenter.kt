package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterClockStyle
import com.luckyzyx.luckytool.hook.scope.systemui.EnableNotificationAlignBothSides
import com.luckyzyx.luckytool.hook.scope.systemui.ForceDisplayMediaPlayer
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveControlCenterDateComma
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveControlCenterMydevice
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveControlCenterUserSwitcher
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarControlCenter : YukiBaseHooker() {
    override fun onHook() {
        //控制中心时钟样式
        loadHooker(ControlCenterClockStyle)

        //移除控制中心日期逗号
        if (prefs(ModulePrefs).getBoolean("remove_control_center_date_comma", false)) {
            loadHooker(RemoveControlCenterDateComma)
        }
        //移除控制中心多用户
        if (prefs(ModulePrefs).getBoolean("remove_control_center_user_switcher", false)) {
            if (SDK < A13) loadHooker(RemoveControlCenterUserSwitcher)
        }
        //移除控制中心我的设备
        if (prefs(ModulePrefs).getBoolean("remove_control_center_mydevice", false)) {
            if (SDK >= A13) loadHooker(RemoveControlCenterMydevice)
        }
        //强制显示媒体播放器
        if (prefs(ModulePrefs).getBoolean("force_display_media_player", false)) {
            loadHooker(ForceDisplayMediaPlayer)
        }
        //通知左右对齐
        if (prefs(ModulePrefs).getBoolean("enable_notification_align_both_sides", false)) {
            loadHooker(EnableNotificationAlignBothSides)
        }
    }
}