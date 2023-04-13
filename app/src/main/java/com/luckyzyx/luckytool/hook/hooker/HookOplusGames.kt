package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.oplusgames.*
import com.luckyzyx.luckytool.utils.tools.ModulePrefs


object HookOplusGames : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.oplus.games") {
            //HookCloudConditionFeature
            loadHooker(CloudConditionFeature)

            //游戏滤镜-->Root检测
            if (prefs(ModulePrefs).getBoolean("remove_root_check", false)) {
                loadHooker(RemoveRootCheck)
            }
            //简洁页面
            if (prefs(ModulePrefs).getBoolean("remove_startup_animation", false)) {
                loadHooker(RemoveStartupAnimation)
            }
            //启用开发者选项
            if (prefs(ModulePrefs).getBoolean("enable_developer_page", false)) {
                loadHooker(EnableDeveloperPage)
            }
            //启用赛事支持模式
            if (prefs(ModulePrefs).getBoolean("enable_support_competition_mode", false)) {
                loadHooker(EnableSupportCompetitionMode)
            }
            //启用X模式
            if (prefs(ModulePrefs).getBoolean("enable_x_mode_feature", false)) {
                loadHooker(EnableXModeFeature)
            }
            //移除赛事模式音效
            if (prefs(ModulePrefs).getBoolean("remove_competition_mode_sound", false)) {
                loadHooker(RemoveCompetitionModeSound)
            }
            //移除游戏助手福利页面
            if (prefs(ModulePrefs).getBoolean("remove_welfare_page", false)) {
                loadHooker(RemoveWelfarePage)
            }

            //Search GamePerfModeModel -> perf_touch_response_extreme_rb
            //<string name="perf_touch_response_extreme_response">极致触控</string>
            //mPerfTouchResponseExtreme click -> setTouchResponse

            //闪电启动
            //business.secondarypanel.view.GameFastStartFloatView
//            <string name="fast_start_title_tips">OSWAP 闪电启动中</string>
//            <string name="fast_start_title_tips_no_oswap">闪电启动中</string>
//            <string name="fast_start_title_success">闪电启动成功，游戏快人一步</string>
//            <string name="fast_start_success_save_time_tip">闪电启动成功，本次节省 %d s</string>


            //GPA极限稳帧
            //com.oplus.cosa.gpalibrary.core.GpaCore

            //network_speed_vip -> oppo_acc

            //游戏变声VIP (作废) -> com.oplus.games.account.bean.VipInfoBean.VipInfosDTO
        }

//        if (packageName == "com.oplus.cosa") { }
    }
}