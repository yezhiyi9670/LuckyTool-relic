package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.browser.HookBrowser
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookBrowser : YukiBaseHooker() {
    override fun onHook() {
        //移除天气页面广告
        if (prefs(ModulePrefs).getBoolean("remove_weather_page_ads", false)) {
            loadHooker(HookBrowser)
        }
    }
}