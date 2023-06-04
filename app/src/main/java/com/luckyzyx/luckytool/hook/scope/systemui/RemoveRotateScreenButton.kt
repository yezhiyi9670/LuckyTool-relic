package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass

object RemoveRotateScreenButton : YukiBaseHooker() {
    override fun onHook() {
        //Source FloatingRotationButton
        VariousClass(
            "com.android.systemui.statusbar.phone.FloatingRotationButton", //A11
            "com.android.systemui.navigationbar.gestural.FloatingRotationButton", //A12
            "com.android.systemui.shared.rotation.FloatingRotationButton" //A13
        ).hook {
            injectMember {
                constructor { param { it[0] == ContextClass } }
                afterHook {
                    field { name = "mKeyButtonView" }.get(instance).cast<View>()?.isVisible = false
                }
            }
        }
    }
}