package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object HideInActiveSignalLabelsGen2x2 : YukiBaseHooker() {
    override fun onHook() {
        //SourceMobileIconSets -> Companion -> config_isSystemUiExpSignalUi
        VariousClass(
            "com.oplus.systemui.statusbar.policy.MobileIconSets\$Companion", //C13
            "com.oplusos.systemui.statusbar.policy.MobileIconSets\$Companion" //C14
        ).hook {
            injectMember {
                method { name = "getVolteIcon" }
                beforeHook {
                    val position = args().last().cast<Int>() ?: return@beforeHook
                    val clazz =
                        if (SDK >= A14) "com.oplusos.systemui.statusbar.policy.MobileIconSets".toClass(
                            initialize = true
                        ) else "com.oplus.systemui.statusbar.policy.MobileIconSets".toClass(
                            initialize = true
                        )
                    val volteIconEx =
                        clazz.field { name = "VOLTE_ICON_EX" }.get().cast<IntArray>()
                            ?: return@beforeHook
                    result = if (position < 0 || position >= volteIconEx.size) 0
                    else volteIconEx[position]
                }
            }
        }
    }
}