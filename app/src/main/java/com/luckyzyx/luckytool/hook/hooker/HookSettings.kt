package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.settings.DarkModeList
import com.luckyzyx.luckytool.hook.scope.settings.EnableStatusBarClockFormat
import com.luckyzyx.luckytool.hook.scope.settings.ForceDisplayBottomGoogleSettings
import com.luckyzyx.luckytool.hook.scope.settings.ForceDisplayDCBackLightMode
import com.luckyzyx.luckytool.hook.scope.settings.HookAppDetails
import com.luckyzyx.luckytool.hook.scope.settings.HookAppFeatureProvider
import com.luckyzyx.luckytool.hook.scope.settings.HookIris5Controller
import com.luckyzyx.luckytool.hook.scope.settings.RemoveDpiRestartRecovery
import com.luckyzyx.luckytool.hook.scope.settings.RemoveSettingsBottomLaboratory
import com.luckyzyx.luckytool.hook.scope.settings.RemoveTopAccountDisplay
import com.luckyzyx.luckytool.utils.ModulePrefs


object HookSettings : YukiBaseHooker() {
    override fun onHook() {
        //HookAppFeatureProvider
        loadHooker(HookAppFeatureProvider)

        //暗色模式列表
        if (prefs(ModulePrefs).getBoolean("dark_mode_list_enable", false)) {
            loadHooker(DarkModeList)
        }
        //应用详情页
        loadHooker(HookAppDetails)
        //移除顶部账号显示
        if (prefs(ModulePrefs).getBoolean("remove_top_account_display", false)) {
            loadHooker(RemoveTopAccountDisplay)
        }
        //视频动态插帧
        if (prefs(ModulePrefs).getBoolean("video_frame_insertion_support_2K120", false)) {
            loadHooker(HookIris5Controller)
        }
        //移除DPI重启恢复
        if (prefs(ModulePrefs).getBoolean("remove_dpi_restart_recovery", false)) {
            loadHooker(RemoveDpiRestartRecovery)
        }
        //强制显示低亮无频闪护眼
        if (prefs(ModulePrefs).getBoolean("force_display_dc_backlight_mode", false)) {
            loadHooker(ForceDisplayDCBackLightMode)
        }
        //强制显示设置底部Google
        if (prefs(ModulePrefs).getBoolean("force_display_bottom_google_settings", false)) {
            loadHooker(ForceDisplayBottomGoogleSettings)
        }
        //移除设置底部实验室
        if (prefs(ModulePrefs).getBoolean("remove_settings_bottom_laboratory", false)) {
            loadHooker(RemoveSettingsBottomLaboratory)
        }
        //启用状态栏时钟格式
        if (prefs(ModulePrefs).getBoolean("enable_statusbar_clock_format", false)) {
            loadHooker(EnableStatusBarClockFormat)
        }

        //oplus_display_settings_screen_timeout_title
        //screen_timeout
        //dream_timeout_values

        //HookSettingsFeature -> memc
        //loadHooker(HookSettingsFeature)

        //persist.oplus.display.vrr
        //persist.oplus.display.vrr.adfr

        //指纹快速解锁 盲解
//        findClass("com.oplus.settings.feature.fingerprint.controller.FingerprintFastUnlockPreferenceController").hook {
//            injectMember {
//                method {
//                    name = "getAvailabilityStatus"
//                }
//                if (false) replaceTo(0)
//            }
//        }

        //keep_screen_on -> 充电时屏幕不休眠
        //settings put global stay_on_while_plugged_in 7
        //com.android.settings.development.StayAwakePreferenceController

        //<string name="airplane_mode">飞行模式</string>
        //com.oplus.settings.feature.network.AirplaneController -> setAirplaneModeOn

        //safecenter_prohibit_monitor safecenter_prohibit_monitor_title -> 禁用权限监控
        //com.oplus.settings.feature.othersettings.development.ProhibitMonitorPreferenceController
    }
}