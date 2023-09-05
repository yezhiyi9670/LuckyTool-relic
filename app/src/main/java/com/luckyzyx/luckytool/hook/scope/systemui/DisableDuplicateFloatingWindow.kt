package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod

object DisableDuplicateFloatingWindow : YukiBaseHooker() {
    override fun onHook() {
        //Source ClipboardOverlayController
        val controllerCls = "com.android.systemui.clipboardoverlay.ClipboardOverlayController"
        val clazz = if (controllerCls.toClass().hasMethod { name = "showSinglePreview" }) {
            "com.android.systemui.clipboardoverlay.ClipboardOverlayController" //C13
        } else "com.android.systemui.clipboardoverlay.ClipboardOverlayView" //C14
        findClass(clazz).hook {
            injectMember {
                method { name = "showSinglePreview" }
                afterHook {
                    args().first().cast<View>()?.isVisible = false
                    when (instanceClass.simpleName) {
                        "ClipboardOverlayView" -> instance<View>().isVisible = false
                        "ClipboardOverlayController" -> field { name = "mView" }.get(instance)
                            .cast<View>()?.isVisible = false
                    }
                }
            }
        }
    }
}