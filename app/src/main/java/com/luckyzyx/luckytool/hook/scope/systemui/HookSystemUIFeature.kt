package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookSystemUIFeature : YukiBaseHooker() {
    override fun onHook() {
        val autoBrightnessMode =
            prefs(ModulePrefs).getString("set_auto_brightness_button_mode", "0")
        val notifyImportance =
            prefs(ModulePrefs).getBoolean("enable_notification_importance_classification", false)
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
            injectMember {
                method { name = "isOriginNotificationBehavior" }
                if (notifyImportance) replaceToTrue()
            }
        }

        //Source NotificationAppFeatureOption
        findClass("com.oplusos.systemui.common.util.NotificationAppFeatureOption").hook {
            injectMember {
                method { name = "originNotificationBehavior" }
                if (notifyImportance) replaceToTrue()
            }
        }
    }
}