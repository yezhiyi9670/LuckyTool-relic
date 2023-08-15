package com.luckyzyx.luckytool.hook.scope.uiengine

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object SetAodStyleMode : YukiBaseHooker() {

    override fun onHook() {
        val mode = prefs(ModulePrefs).getString("set_aod_style_mode", "0")
        //Source ProductFlavorOption
        findClass("com.oplus.egview.util.ProductFlavorOption").hook {
            injectMember {
                method { name = "isFlavorTwoDevice" }
                when (mode) {
                    "1" -> replaceToTrue()
                    "2" -> replaceToFalse()
                }
            }
        }
    }
}