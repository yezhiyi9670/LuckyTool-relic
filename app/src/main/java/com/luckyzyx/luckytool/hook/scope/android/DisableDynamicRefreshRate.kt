package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object DisableDynamicRefreshRate : YukiBaseHooker() {

    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("disable_dynamic_refresh_rate", false)

        //Source OPlusRefreshRateManager
        //persist.oplus.display.vrr persist.oplus.display.vrr.adfr
        "com.oplus.vrr.OPlusRefreshRateManager".toClass().apply {
            method { name = "hasVRRFeature" }.hook {
                if (isEnable) replaceToFalse()
            }
            method { name = "hasADFRFeature" }.hook {
                if (isEnable) replaceToFalse()
            }
        }
    }
}