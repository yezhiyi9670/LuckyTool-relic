package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookSystemUIFeature : YukiBaseHooker() {
    override fun onHook() {
        loadHooker(HookFeatureOption)
        loadHooker(HookStatusBarFeature)
        loadHooker(HookNotificationAppFeature)
        loadHooker(HookFlavorOneFeature)
    }

    private object HookFeatureOption : YukiBaseHooker() {
        override fun onHook() {
            //自动亮度 com.android.systemui.remove_auto_brightness
            val autoBrightnessMode =
                prefs(ModulePrefs).getString("set_auto_brightness_button_mode", "0")
            //全屏充电动画 com.android.systemui.support_fullscreen_charge_anim
            val fullScreenChargeAnim =
                prefs(ModulePrefs).getString("set_full_screen_charging_animation_mode", "0")
            //通知重要性 com.android.systemui.origin_notification_behavior
            val notifyImportance =
                prefs(ModulePrefs).getBoolean(
                    "enable_notification_importance_classification",
                    false
                )
            //高斯模糊
            val enableBlur =
                prefs(ModulePrefs).getBoolean("force_enable_systemui_blur_feature", false)
            //右侧音量条
            val rightVolume =
                prefs(ModulePrefs).getBoolean("enable_right_volume_bar_display", false)
            //音量对话框背景透明度
            var volumeBlur =
                prefs(ModulePrefs).getInt("custom_volume_dialog_background_transparency", -1)
            dataChannel.wait<Int>("custom_volume_dialog_background_transparency") {
                volumeBlur = it
            }
            //锁屏充电显示瓦数
            var showWattage =
                prefs(ModulePrefs).getBoolean("force_lock_screen_charging_show_wattage", false)
            dataChannel.wait<Boolean>("force_lock_screen_charging_show_wattage") {
                showWattage = it
            }
            //WARP充电器样式
            var warpCharge =
                prefs(ModulePrefs).getString("set_lock_screen_warp_charging_style", "0")
            dataChannel.wait<String>("set_lock_screen_warp_charging_style") { warpCharge = it }

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
                    if (volumeBlur > -1) replaceToFalse()
                }
                if (instanceClass.hasMethod { name = "isAiSdr2HdrSupport" }) {
                    injectMember {
                        method { name = "isAiSdr2HdrSupport" }
                        if (enableBlur) replaceToFalse()
                    }
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
                if (instanceClass.hasMethod { name = "isSupportShowWattage" }) {
                    injectMember {
                        method { name = "isSupportShowWattage" }
                        if (warpCharge == "2" && showWattage) replaceToTrue()
                    }
                }
                if (SDK >= A13 && instanceClass.hasMethod { name = "isUseWarpCharge" }) {
                    injectMember {
                        method { name = "isUseWarpCharge" }
                        beforeHook {
                            when (warpCharge) {
                                "1" -> resultTrue()
                                "2" -> resultFalse()
                                else -> return@beforeHook
                            }
                        }
                    }
                }
            }
        }
    }

    private object HookStatusBarFeature : YukiBaseHooker() {
        override fun onHook() {
            //隐藏未使用信号标签 config_isSystemUiExpSignalUi
            val hideSignalLabels =
                prefs(ModulePrefs).getBoolean("hide_inactive_signal_labels_gen2x2", false)

            //Source StatusBarFeatureOption
            findClass("com.oplusos.systemui.statusbar.feature.StatusBarFeatureOption").hook {
                injectMember {
                    method { name = "isSystemUiExpSignalUi" }
                    if (hideSignalLabels) replaceToTrue()
                }
            }
        }
    }

    private object HookNotificationAppFeature : YukiBaseHooker() {
        override fun onHook() {
            //通知重要性 com.android.systemui.origin_notification_behavior
            val notifyImportance = prefs(ModulePrefs).getBoolean(
                "enable_notification_importance_classification", false
            )
            //高斯模糊
            val enableBlur =
                prefs(ModulePrefs).getBoolean("force_enable_systemui_blur_feature", false)

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

    private object HookFlavorOneFeature : YukiBaseHooker() {
        override fun onHook() {
            //全局搜索按钮
            val searchBtnMode =
                prefs(ModulePrefs).getString("set_control_center_search_button_mode", "0")

            //Source FlavorOneFeatureOption
            findClass("com.oplusos.systemui.common.feature.FlavorOneFeatureOption").hook {
                if (instanceClass.hasMethod { name = "isSupportSearch" }) {
                    injectMember {
                        method { name = "isSupportSearch" }
                        beforeHook {
                            when (searchBtnMode) {
                                "1" -> resultTrue()
                                "2" -> resultFalse()
                                else -> return@beforeHook
                            }
                        }
                    }
                } else return
            }
        }
    }
}