package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.browser.RemoveAdsAtDownloadPageBottom
import com.luckyzyx.luckytool.hook.scope.browser.RemoveAdsFromDownloadDialog
import com.luckyzyx.luckytool.hook.scope.browser.RemoveAdsFromWeatherPage
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookBrowser : YukiBaseHooker() {
    override fun onHook() {
        //移除下载对话框广告
        if (prefs(ModulePrefs).getBoolean("remove_ads_from_download_dialog", false)) {
            loadHooker(RemoveAdsFromDownloadDialog)
        }
        //移除下载页面底部广告
        if (prefs(ModulePrefs).getBoolean("remove_ads_at_download_page_bottom", false)) {
            loadHooker(RemoveAdsAtDownloadPageBottom)
        }
        //移除天气页面广告
        if (prefs(ModulePrefs).getBoolean("remove_ads_from_weather_page", false)) {
            loadHooker(RemoveAdsFromWeatherPage)
        }
    }
}