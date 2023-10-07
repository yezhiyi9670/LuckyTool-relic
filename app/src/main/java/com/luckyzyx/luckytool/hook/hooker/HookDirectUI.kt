package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.directui.RemoveTouchAppRecommendCard
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookDirectUI : YukiBaseHooker() {
    override fun onHook() {
        //移除应用推广卡片
        if (prefs(ModulePrefs).getBoolean("remove_touch_app_recommend_card", false)) {
            loadHooker(RemoveTouchAppRecommendCard)
        }
    }
}