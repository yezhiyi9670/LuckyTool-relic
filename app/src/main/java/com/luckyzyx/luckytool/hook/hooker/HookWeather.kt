package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.weather.RemoveWeatherAD
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookWeather : YukiBaseHooker() {
    override fun onHook() {
        //移除天气广告
        if (prefs(ModulePrefs).getBoolean("remove_onplusWeather_ad", false)) {
            loadHooker(RemoveWeatherAD)
        }
    }
}