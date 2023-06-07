package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object DisableDynamicRefreshRate : YukiBaseHooker() {

    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("disable_dynamic_refresh_rate", false)
        //Source OPlusRefreshRateManager
        //persist.oplus.display.vrr persist.oplus.display.vrr.adfr
        findClass("com.oplus.vrr.OPlusRefreshRateManager").hook {
            injectMember {
                method { name = "hasVRRFeature" }
                if (isEnable) replaceToFalse()
            }
            injectMember {
                method { name = "hasADFRFeature" }
                if (isEnable) replaceToFalse()
            }
        }
    }
}