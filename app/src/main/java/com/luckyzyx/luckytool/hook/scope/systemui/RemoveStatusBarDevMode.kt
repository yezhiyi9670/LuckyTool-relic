package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveStatusBarDevMode : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemPromptController
        VariousClass(
            "com.coloros.systemui.statusbar.policy.ColorSystemPromptController",
            "com.oplusos.systemui.statusbar.policy.SystemPromptController",
            "com.oplus.systemui.statusbar.controller.SystemPromptController" //C14
        ).toClass().apply {
            method { name = "updateDeveloperMode" }.hook {
                intercept()
            }
        }
    }
}