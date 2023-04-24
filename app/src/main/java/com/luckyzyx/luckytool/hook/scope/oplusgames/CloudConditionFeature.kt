package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.AnyClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.MapClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object CloudConditionFeature : YukiBaseHooker() {
    override fun onHook() {
        //Source GpuSettingHelper
        val gpuControl = prefs(ModulePrefs).getBoolean("enable_adreno_gpu_controller", false)
        //Source GameFrameInsertInfo
        val pickleFeature =
            prefs(ModulePrefs).getBoolean("enable_increase_fps_limit_feature", false)
        val fpsFeature = prefs(ModulePrefs).getBoolean("enable_increase_fps_feature", false)
        val powerFeature = prefs(ModulePrefs).getBoolean("enable_optimise_power_feature", false)
        //Source Feats
        val gtMode = prefs(ModulePrefs).getBoolean("enable_gt_mode_feature", false)
        //Source CoolingBackClipHelper
        val xMode = prefs(ModulePrefs).getBoolean("enable_x_mode_feature", false)
        //Source SuperResolutionHelper
        val superResolution =
            prefs(ModulePrefs).getBoolean("enable_super_resolution_feature", false)
        //Source CloudConditionUtil
        val oneplusCharacteristic =
            prefs(ModulePrefs).getBoolean("enable_one_plus_characteristic", false)

        //Source OplusFeatureHelper
        findClass("com.oplus.addon.OplusFeatureHelper\$Companion").hook {
            injectMember {
                method {
                    param { it[1] == StringClass && it[2] == BooleanType && it[3] == IntType && it[4] == AnyClass }
                    paramCount = 5
                    returnType = BooleanType
                }
                beforeHook {
                    when (args(1).string()) {
                        //feature -> isSupportFrameInsert
                        "oplus.software.display.game.memc_enable" -> if (pickleFeature || fpsFeature || powerFeature) resultTrue()
                        //插帧pickleFeature -> isSupportUniqueFrameInsert
                        "oplus.software.display.game.memc_increase_fps_limit_mode" -> if (pickleFeature) resultTrue()
                        //提升帧率feature -> isSupportIncreaseFps
                        "oplus.software.display.game.memc_increase_fps_mode" -> if (fpsFeature) resultTrue()
                        //优化功耗feature -> isSupportOptimisePower
                        "oplus.software.display.game.memc_optimise_power_mode" -> if (powerFeature) resultTrue()
                        //GPU控制器 -> isSupportGpuControl
                        "oplus.gpu.controlpanel.support" -> if (gpuControl) resultTrue()
                        //GT模式 -> isSupportGtMode
                        "oplus.software.support.gt.mode" -> if (gtMode) resultTrue()
                        //超级分辨率 -> issupportSupperResolution
                        "oplus.software.display.game.sr_enable" -> if (superResolution) resultTrue()
                    }
                }
            }
        }

        //Source CloudConditionUtil
        findClass("com.coloros.gamespaceui.config.cloud.CloudConditionUtil").hook {
            injectMember {
                method {
                    param(StringClass, MapClass, IntType, AnyClass)
                    paramCount = 4
                    returnType = BooleanType
                }
                beforeHook {
                    when (args(0).string()) {
                        //pickle插帧云控 -> cloudFrameInsertEnable
                        "frame_insert" -> if (pickleFeature) resultTrue()
                        //提升帧率云控 -> cloudIncreaseFpsEnable
                        "increase_fps" -> if (fpsFeature) resultTrue()
                        //优化功耗云控 -> cloudOptimisePowerEnable
                        "optimise_power" -> if (powerFeature) resultTrue()
                        //GPU控制器云控 -> isCloudSupportGpuControlPanel
                        "gpu_control_panel" -> if (gpuControl) resultTrue()
                        //X模式 -> isSupportXMode
                        "cool_back_clip_blacklist" -> if (xMode) resultTrue()
                        //OnePlus特性
                        "one_plus_characteristic" -> if (oneplusCharacteristic) resultTrue()
                    }
                }
            }
            injectMember {
                method {
                    param(StringClass, MapClass)
                    paramCount = 2
                    returnType = BooleanType
                }
                beforeHook {
                    when (args(0).string()) {
                        //超级分辨率云控 -> cloudSRSupport
                        "super_resolution_config" -> if (superResolution) resultTrue()
                    }
                }
            }
        }
    }
}