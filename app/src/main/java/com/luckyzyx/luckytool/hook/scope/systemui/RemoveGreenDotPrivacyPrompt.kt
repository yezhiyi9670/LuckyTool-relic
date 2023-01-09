package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveGreenDotPrivacyPrompt : YukiBaseHooker() {
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
        //二次处理
        "com.oplusos.systemui.statusbar.events.OplusPrivacyDotViewController".toClassOrNull()?.hook {
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