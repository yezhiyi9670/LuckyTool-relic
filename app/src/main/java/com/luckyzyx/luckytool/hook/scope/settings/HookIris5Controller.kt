package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object HookIris5Controller : YukiBaseHooker() {
    override fun onHook() {
        val isVideoFrameInsertion = true
        val isVideoDisplayEnhancement =
            false //prefs(ModulePrefs).getBoolean("video_display_enhancement_support_2K120", false)
        val isVideoSuperResolution =
            false //prefs(ModulePrefs).getBoolean("video_super_resolution_support_2K120", false)

        //Source Iris5MotionFluencySwitchController
        "com.oplus.settings.feature.display.controller.Iris5MotionFluencySwitchController".toClass()
            .apply {
                method { name = "is2kReject" }.hook {
                    if (isVideoFrameInsertion) replaceToFalse()
                }
                method { name = "isSupport120With2K" }.hook {
                    if (isVideoFrameInsertion) replaceToTrue()
                }
            }
        //Source Iris5MotionFluencyController
        "com.oplus.settings.feature.display.controller.Iris5MotionFluencyController".toClass()
            .apply {
                method { name = "is2kReject" }.hook {
                    if (isVideoFrameInsertion) replaceToFalse()
                }
                method { name = "isSupport120With2K" }.hook {
                    if (isVideoFrameInsertion) replaceToTrue()
                }
            }
        //Source Iris5VideoDisplayEnhancementController
        "com.oplus.settings.feature.display.controller.Iris5VideoDisplayEnhancementController".toClass()
            .apply {
                method { name = "is2kReject" }.hook {
                    if (isVideoDisplayEnhancement) replaceToFalse()
                }
                method { name = "isSupport120With2K" }.hook {
                    if (isVideoDisplayEnhancement) replaceToTrue()
                }
            }
        //Source Iris5VideoSuperResolutionController
        "com.oplus.settings.feature.display.controller.Iris5VideoSuperResolutionController".toClass()
            .apply {
                method { name = "is2kReject" }.hook {
                    if (isVideoSuperResolution) replaceToFalse()
                }
                method { name = "isSupport120With2K" }.hook {
                    if (isVideoSuperResolution) replaceToTrue()
                }
            }
    }
}