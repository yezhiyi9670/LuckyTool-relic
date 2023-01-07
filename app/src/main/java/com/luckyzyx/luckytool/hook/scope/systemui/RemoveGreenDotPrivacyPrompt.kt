package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveGreenDotPrivacyPrompt : YukiBaseHooker() {
    override fun onHook() {
        //Source PrivacyDotViewController
        findClass("com.android.systemui.statusbar.events.PrivacyDotViewController").hook {
            injectMember {
                method {
                    name = "showDotView"
                    paramCount = 2
                }
                intercept()
            }
            injectMember {
                method {
                    name = "updateDesignatedCorner"
                    paramCount = 2
                }
                intercept()
            }
        }
    }
}