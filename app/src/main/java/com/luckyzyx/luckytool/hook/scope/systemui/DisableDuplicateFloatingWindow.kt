package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object DisableDuplicateFloatingWindow : YukiBaseHooker() {
    override fun onHook() {
        //Source ClipboardOverlayController
        findClass("com.android.systemui.clipboardoverlay.ClipboardOverlayController").hook {
            injectMember {
                method { name = "showSinglePreview" }
                afterHook {
                    args().first().cast<View>()?.isVisible = false
                    field { name = "mView" }.get(instance).cast<View>()?.isVisible = false
                }
            }
        }
    }
}