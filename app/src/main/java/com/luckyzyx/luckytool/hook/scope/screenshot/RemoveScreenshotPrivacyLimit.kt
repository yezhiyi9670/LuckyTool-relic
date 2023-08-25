package com.luckyzyx.luckytool.hook.scope.screenshot

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveScreenshotPrivacyLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source ScreenshotContext
        findClass("com.oplus.screenshot.screenshot.core.ScreenshotContext").hook {
            injectMember {
                method { name = "setScreenshotReject" }
                beforeHook {
                    val rejectCls = args().first().any()?.javaClass ?: return@beforeHook
                    if (rejectCls.isEnum.not()) return@beforeHook
                    val enumConstants = rejectCls.enumConstants ?: return@beforeHook
                    enumConstants.forEach {
                        if (it.toString() == "ACCEPTED") args().first().set(it)
                    }
                }
            }
            injectMember {
                method { name = "setLongshotReject" }
                beforeHook {
                    val rejectCls = args().first().any()?.javaClass ?: return@beforeHook
                    if (rejectCls.isEnum.not()) return@beforeHook
                    val enumConstants = rejectCls.enumConstants ?: return@beforeHook
                    enumConstants.forEach {
                        if (it.toString() == "ACCEPTED") args().first().set(it)
                    }
                }
            }
        }
    }
}