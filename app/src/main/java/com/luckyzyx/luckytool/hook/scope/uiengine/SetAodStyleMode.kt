package com.luckyzyx.luckytool.hook.scope.uiengine

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object SetAodStyleMode : YukiBaseHooker() {

    override fun onHook() {
        val mode = prefs(ModulePrefs).getString("set_aod_style_mode", "0")

        //Source ProductFlavorOption
        "com.oplus.egview.util.ProductFlavorOption".toClass().apply {
            method { name = "isFlavorTwoDevice" }.hook {
                when (mode) {
                    "1" -> replaceToTrue()
                    "2" -> replaceToFalse()
                }
            }
        }
    }
}