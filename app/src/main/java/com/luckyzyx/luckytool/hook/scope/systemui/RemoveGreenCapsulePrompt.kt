package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveGreenCapsulePrompt : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemPromptView
        findClass("com.oplusos.systemui.statusbar.widget.SystemPromptView").hook {
            injectMember {
                method {
                    name = "updateViewVisible"
                }
                intercept()
            }
            injectMember {
                method {
                    name = "setViewVisibleByDisable"
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
        }
    }
}