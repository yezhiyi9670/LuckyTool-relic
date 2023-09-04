package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object DisableDuplicateFloatingWindow : YukiBaseHooker() {
    override fun onHook() {
        //Source ClipboardOverlayController
        VariousClass(
            "com.android.systemui.clipboardoverlay.ClipboardOverlayController", //C13
            "com.android.systemui.clipboardoverlay.ClipboardOverlayView" //C14
        ).hook {
            injectMember {
                method { name = "showSinglePreview" }
                afterHook {
                    args().first().cast<View>()?.isVisible = false
                    if (SDK >= A14) instance<View>().isVisible = false
                    else field { name = "mView" }.get(instance).cast<View>()?.isVisible = false
                }
            }
        }
    }
}