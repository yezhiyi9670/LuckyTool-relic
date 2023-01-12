package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.*
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object StatusBarControlCenter : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //控制中心时钟样式
            loadHooker(ControlCenterClockStyle)

            //移除控制中心日期逗号
            if (prefs(XposedPrefs).getBoolean("remove_control_center_date_comma", false)) {
                loadHooker(RemoveControlCenterDateComma)
            }
            //移除控制中心多用户
            if (prefs(XposedPrefs).getBoolean("remove_control_center_user_switcher", false)) {
                if (SDK < A13) loadHooker(RemoveControlCenterUserSwitcher)
            }
            //移除控制中心我的设备
            if (prefs(XposedPrefs).getBoolean("remove_control_center_mydevice", false)) {
                if (SDK >= A13) loadHooker(RemoveControlCenterMydevice)
            }
            //强制显示媒体播放器
            if (prefs(XposedPrefs).getBoolean("force_display_media_player", false)) {
                loadHooker(ForceDisplayMediaPlayer)
            }

            //状态栏磁贴列数
            if (prefs(XposedPrefs).getBoolean("control_center_tile_enable", false)) {
                if (SDK >= A13) loadHooker(ControlCenterTilesColumnV13) else {
                    loadHooker(ControlCenterTilesColumn)
                }
            }
        }
    }
}