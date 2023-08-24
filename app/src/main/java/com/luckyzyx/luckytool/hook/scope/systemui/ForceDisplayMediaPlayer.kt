package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object ForceDisplayMediaPlayer : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusQsMediaCarouselController
        findClass("com.oplus.systemui.qs.media.OplusQsMediaCarouselController").hook {
            injectMember {
                method { name = "setCurrentMediaData" }
                afterHook {
                    val mediaModeChangeListener =
                        field { name = "mediaModeChangeListener" }.get(instance).any()
                            ?: return@afterHook
                    mediaModeChangeListener.current().method {
                        name = "onChanged"
                    }.call(true)
                }
            }
            injectMember {
                method { name = "setMediaModeChangeListener" }
                afterHook {
                    val mediaModeChangeListener = args().first().any() ?: return@afterHook
                    mediaModeChangeListener.current().method {
                        name = "onChanged"
                    }.call(true)
                }
            }
        }
    }
}