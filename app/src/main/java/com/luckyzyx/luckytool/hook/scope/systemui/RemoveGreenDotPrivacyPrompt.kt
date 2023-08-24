package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.core.YukiMemberHookCreator
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object RemoveGreenDotPrivacyPrompt : YukiBaseHooker() {
    override fun onHook() {
        //Source PrivacyDotViewController
        findClass("com.android.systemui.statusbar.events.PrivacyDotViewController").hook {
            hookMethod()
        }
        //二次处理
        "com.oplusos.systemui.statusbar.events.OplusPrivacyDotViewController".toClassOrNull()
            ?.hook {
                hookMethod()
            }
        if (SDK < A14) return
        findClass("com.oplus.systemui.privacy.OplusPrivacyDotViewController").hook {
            hookMethod()
        }
    }

    private fun YukiMemberHookCreator.hookMethod() {
        injectMember {
            method { name = "showDotView";paramCount = 2 }
            intercept()
        }
        injectMember {
            method { name = "updateDesignatedCorner";paramCount = 2 }
            intercept()
        }
    }
}