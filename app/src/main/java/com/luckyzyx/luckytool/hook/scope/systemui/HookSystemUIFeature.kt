package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookSystemUIFeature : YukiBaseHooker() {
    override fun onHook() {
        val autoBrightnessMode =
            prefs(ModulePrefs).getString("set_auto_brightness_button_mode", "0")
        //Source FeatureOption
        findClass("com.oplusos.systemui.common.feature.FeatureOption").hook {
            injectMember {
                method { name = "shouldRemoveAutoBrightness" }
                beforeHook {
                    when (autoBrightnessMode) {
                        "1" -> resultFalse()
                        "2" -> resultTrue()
                        else -> return@beforeHook
                    }
                }
            }
        }
    }
}