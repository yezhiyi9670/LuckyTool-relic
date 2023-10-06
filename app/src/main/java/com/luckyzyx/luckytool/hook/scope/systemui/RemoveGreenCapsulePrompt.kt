package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object RemoveGreenCapsulePrompt : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemPromptView
        VariousClass(
            "com.oplusos.systemui.statusbar.widget.SystemPromptView", //C13
            "com.oplus.systemui.statusbar.widget.SystemPromptView" //C14
        ).toClass().apply {
            method { name = "updateViewVisible" }.hook {
                before {
                    field { name = "disable" }.get(instance).setTrue()
                }
            }
            method {
                name = if (SDK >= A14) "disable"
                else "setViewVisibleByDisable"
            }.hook {
                before { args().first().setTrue() }
            }
        }
    }
}