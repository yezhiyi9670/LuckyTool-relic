package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookSystemUIFeature : YukiBaseHooker() {
    override fun onHook() {
        //自动亮度 com.android.systemui.remove_auto_brightness
        val autoBrightnessMode =
            prefs(ModulePrefs).getString("set_auto_brightness_button_mode", "0")
        //全屏充电动画 com.android.systemui.support_fullscreen_charge_anim
        val fullScreenChargeAnim =
            prefs(ModulePrefs).getString("set_full_screen_charging_animation_mode", "0")
        //通知重要性 com.android.systemui.origin_notification_behavior
        val notifyImportance =
            prefs(ModulePrefs).getBoolean("enable_notification_importance_classification", false)
        //隐藏未使用信号标签 config_isSystemUiExpSignalUi
        val hideSignalLabels =
            prefs(ModulePrefs).getBoolean("hide_inactive_signal_labels_gen2x2", false)
        //高斯模糊
        val enableBlur = prefs(ModulePrefs).getBoolean("force_enable_systemui_blur_feature", false)
        //右侧音量条
        val rightVolume = prefs(ModulePrefs).getBoolean("enable_right_volume_bar_display", false)
        //音量对话框背景透明度
        val volumeBlur =
            prefs(ModulePrefs).getInt("custom_volume_dialog_background_transparency", -1) > -1

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
            //指纹快速解锁 盲解
//            injectMember {
//                method { name = "isFpBlindUnlockDisabled" }
//                if (false) replaceToFalse()
//            }
            injectMember {
                method { name = "isOriginNotificationBehavior" }
                if (notifyImportance) replaceToTrue()
            }
            injectMember {
                method { name = "isVolumeBlurDisabled" }
                if (volumeBlur) replaceToFalse()
            }
            if (SDK < A13) injectMember {
                method { name = "isAiSdr2HdrSupport" }
                if (enableBlur) replaceToFalse()
            }
            if (instanceClass.hasMethod { name = "isOplusVolumeKeyInRight" }) {
                injectMember {
                    method { name = "isOplusVolumeKeyInRight" }
                    if (rightVolume) replaceToTrue()
                }
            }
            if (instanceClass.hasMethod { name = "areVolumeAndPowerKeysInRight" }) {
                injectMember {
                    method { name = "areVolumeAndPowerKeysInRight" }
                    if (rightVolume) replaceToTrue()
                }
            }
            if (instanceClass.hasMethod { name = "isSupportFullScreenChargeAnim" }) {
                injectMember {
                    method { name = "isSupportFullScreenChargeAnim" }
                    beforeHook {
                        when (fullScreenChargeAnim) {
                            "1" -> resultTrue()
                            "2" -> resultFalse()
                            else -> return@beforeHook
                        }
                    }
                }
            }
        }

        //Source StatusBarFeatureOption
        findClass("com.oplusos.systemui.statusbar.feature.StatusBarFeatureOption").hook {
            injectMember {
                method { name = "isSystemUiExpSignalUi" }
                if (hideSignalLabels) replaceToTrue()
            }
        }

        //Source NotificationAppFeatureOption
        findClass("com.oplusos.systemui.common.util.NotificationAppFeatureOption").hook {
            injectMember {
                method { name = "originNotificationBehavior" }
                if (notifyImportance) replaceToTrue()
            }
            injectMember {
                method { name = "getGaussBlurDisabled" }
                if (enableBlur) replaceToFalse()
            }
            if (instanceClass.hasMethod { name = "isPanViewBlurDisabled" }) {
                injectMember {
                    method { name = "isPanViewBlurDisabled" }
                    if (enableBlur) replaceToFalse()
                }
            }
        }
    }
}