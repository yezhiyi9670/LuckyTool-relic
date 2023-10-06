package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object RemoveGreenDotPrivacyPrompt : YukiBaseHooker() {
    override fun onHook() {
        //Source PrivacyDotViewController
        "com.android.systemui.statusbar.events.PrivacyDotViewController".toClass().apply {
            hookMethod()
        }
        //二次处理
        //Source OplusPrivacyDotViewController
        "com.oplusos.systemui.statusbar.events.OplusPrivacyDotViewController".toClassOrNull()
            ?.hookMethod()

        if (SDK < A14) return
        //Source OplusPrivacyDotViewController
        "com.oplus.systemui.privacy.OplusPrivacyDotViewController".toClass().apply {
            hookMethod()
        }
    }

    private fun Class<*>.hookMethod() {
        method { name = "showDotView";paramCount = 2 }.hook {
            intercept()
        }
        method { name = "updateDesignatedCorner";paramCount = 2 }.hook {
            intercept()
        }
    }
}