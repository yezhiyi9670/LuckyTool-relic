package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.oplusgames.*
import com.luckyzyx.luckytool.utils.tools.ModulePrefs


object HookOplusGames : YukiBaseHooker() {
    override fun onHook() {

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

//        <string name="game_box_slide_panel_x_mode_title">X 模式</string>
//        <string name="game_performance_mode_x_summary">连接欧加散热背夹后会自动开启 X 模式，X 模式将为手机提供更优异的散热能力，持续稳定系统温度，提升游戏表现。开启X模式可能带来功耗上升与电量消耗加速，低电量情况下可优先选择均衡或低功耗模式。开启方式：开启蓝牙并连接散热背夹。</string>
//        id perf_mode_x_des_container
//        rg_performance

        //Source CoolingBackClipHelper
//        findClass("business.module.perfmode.CoolingBackClipHelper").hook {
//            injectMember {
//                method {
//                    name = "k"
//                    emptyParam()
//                    returnType = BooleanType
//                }
//                replaceToTrue()
//            }
//            injectMember {
//                method {
//                    name = "e"
//                    param(BooleanType)
//                    returnType = BooleanType
//                }
//                replaceToTrue()
//            }
//        }

        //Source GamePerfModeModel
//        findClass("com.coloros.gamespaceui.module.performancemode.GamePerfModeModel").hook {
//            injectMember {
//                method {
//                    name = "S"
//                    returnType = BooleanType
//                }
//                replaceToTrue()
//            }
//        }

        //network_speed_vip -> oppo_acc

        //游戏变声VIP (作废) -> com.oplus.games.account.bean.VipInfoBean.VipInfosDTO
    }
}