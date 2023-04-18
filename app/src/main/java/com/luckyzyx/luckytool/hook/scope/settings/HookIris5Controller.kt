package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object HookIris5Controller : YukiBaseHooker() {
    override fun onHook() {
        val isVideoFrameInsertion = true
        val isVideoDisplayEnhancement = false
        //prefs(ModulePrefs).getBoolean("video_display_enhancement_support_2K120", false)
        val isVideoSuperResolution = false
        //prefs(ModulePrefs).getBoolean("video_super_resolution_support_2K120", false)
        //Source Iris5MotionFluencySwitchController
        findClass("com.oplus.settings.feature.display.controller.Iris5MotionFluencySwitchController").hook {
            injectMember {
                method {
                    name = "is2kReject"
                }
                if (isVideoFrameInsertion) replaceToFalse()
            }
            injectMember {
                method {
                    name = "isSupport120With2K"
                }
                if (isVideoFrameInsertion) replaceToTrue()
            }
        }
        //Source Iris5MotionFluencyController
        findClass("com.oplus.settings.feature.display.controller.Iris5MotionFluencyController").hook {
            injectMember {
                method {
                    name = "is2kReject"
                }
                if (isVideoFrameInsertion) replaceToFalse()
            }
            injectMember {
                method {
                    name = "isSupport120With2K"
                }
                if (isVideoFrameInsertion) replaceToTrue()
            }
        }
        //Source Iris5VideoDisplayEnhancementController
        findClass("com.oplus.settings.feature.display.controller.Iris5VideoDisplayEnhancementController").hook {
            injectMember {
                method {
                    name = "is2kReject"
                }
                if (isVideoDisplayEnhancement) replaceToFalse()
            }
            injectMember {
                method {
                    name = "isSupport120With2K"
                }
                if (isVideoDisplayEnhancement) replaceToTrue()
            }
        }
        //Source Iris5VideoSuperResolutionController
        findClass("com.oplus.settings.feature.display.controller.Iris5VideoSuperResolutionController").hook {
            injectMember {
                method {
                    name = "is2kReject"
                }
                if (isVideoSuperResolution) replaceToFalse()
            }
            injectMember {
                method {
                    name = "isSupport120With2K"
                }
                if (isVideoSuperResolution) replaceToTrue()
            }
        }
    }
}