package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.market.RemoveMarketDownloadPageAppRecommend
import com.luckyzyx.luckytool.hook.scope.market.RemoveMarketSplashPageAppRecommend
import com.luckyzyx.luckytool.hook.scope.market.RemoveMarketUpdatePageAppRecommend
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookMarket : YukiBaseHooker() {
    override fun onHook() {
        //移除商店启动页应用推荐
        if (prefs(ModulePrefs).getBoolean("remove_market_splash_page_app_recommend", false)) {
            loadHooker(RemoveMarketSplashPageAppRecommend)
        }
        //移除商店更新页应用推荐
        if (prefs(ModulePrefs).getBoolean("remove_market_update_page_app_recommend", false)) {
            loadHooker(RemoveMarketUpdatePageAppRecommend)
        }
        //移除商店下载页应用推荐
        if (prefs(ModulePrefs).getBoolean("remove_market_download_page_app_recommend", false)) {
            loadHooker(RemoveMarketDownloadPageAppRecommend)
        }
    }
}