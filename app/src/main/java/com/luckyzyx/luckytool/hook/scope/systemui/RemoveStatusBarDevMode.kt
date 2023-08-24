package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveStatusBarDevMode : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.coloros.systemui.statusbar.policy.ColorSystemPromptController",
            "com.oplusos.systemui.statusbar.policy.SystemPromptController",
            "com.oplus.systemui.statusbar.controller.SystemPromptController" //C14
        ).hook {
            injectMember {
                method { name = "updateDeveloperMode" }
                intercept()
            }
        }
    }
}