package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterClockStyle
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterDateStyle
import com.luckyzyx.luckytool.hook.scope.systemui.EnableNotificationAlignBothSides
import com.luckyzyx.luckytool.hook.scope.systemui.ForceDisplayMediaPlayer
import com.luckyzyx.luckytool.hook.scope.systemui.ForceEnableMediaToggleButton
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveControlCenterMydevice
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveControlCenterUserSwitcher
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveStatusBarBottomNetworkWarn
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarControlCenter : YukiBaseHooker() {
    override fun onHook() {
        //控制中心时钟样式
        loadHooker(ControlCenterClockStyle)

        //移除控制中心日期
        loadHooker(ControlCenterDateStyle)

        //通知两侧对齐
        if (prefs(ModulePrefs).getBoolean("enable_notification_align_both_sides", false)) {
            loadHooker(EnableNotificationAlignBothSides)
        }
        //强制显示媒体播放器
        if (prefs(ModulePrefs).getBoolean("force_display_media_player", false)) {
            loadHooker(ForceDisplayMediaPlayer)
            //强制开启媒体切换按钮
            if (prefs(ModulePrefs).getBoolean("force_enable_media_toggle_button", false)) {
                loadHooker(ForceEnableMediaToggleButton)
            }
        }
        //移除控制中心多用户
        if (prefs(ModulePrefs).getBoolean("remove_control_center_user_switcher", false)) {
            if (SDK < A13) loadHooker(RemoveControlCenterUserSwitcher)
        }
        //移除控制中心我的设备
        if (prefs(ModulePrefs).getBoolean("remove_control_center_mydevice", false)) {
            if (SDK >= A13) loadHooker(RemoveControlCenterMydevice)
        }
        //磁贴底部网络警告
        loadHooker(RemoveStatusBarBottomNetworkWarn)

        //Source ScrimController -> 通知中心长背景颜色
//        findClass("com.android.systemui.statusbar.phone.ScrimController").hook {
//            injectMember {
//                method { name = "updateScrimColor" }
//                beforeHook {
//                    val view = args().first().cast<View>() ?: return@beforeHook
//                    val alpha = args(1).float()
//                    val tint = args().last().int()
//
////                    scrim_in_front
////                    scrim_behind
////                    scrim_notifications
//
//                    if (view.toString().contains("notifications")) {
//                        args().last().set(Color.YELLOW)
//                    }
//                }
//            }
//        }
        //Source ActivatableNotificationView -> 通知背景
//        findClass("com.android.systemui.statusbar.notification.row.ActivatableNotificationView").hook {
//            injectMember {
//                method {
//                    name = "calculateBgColor"
//                }
//                afterHook {
//                    result = Color.RED
//                }
//            }
//        }
    }
}