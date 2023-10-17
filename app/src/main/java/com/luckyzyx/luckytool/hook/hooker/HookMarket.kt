package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.market.RemoveMarketSplashPageAppRecommend
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookMarket : YukiBaseHooker() {
    override fun onHook() {
        //移除商店启动页应用推荐
        if (prefs(ModulePrefs).getBoolean("remove_market_splash_page_app_recommend", false)) {
            loadHooker(RemoveMarketSplashPageAppRecommend)
        }
    }
}