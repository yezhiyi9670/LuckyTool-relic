package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field

object HideInActiveSignalLabelsGen2x2 : YukiBaseHooker() {
    override fun onHook() {
        //SourceMobileIconSets -> Companion
        findClass("com.oplus.systemui.statusbar.policy.MobileIconSets\$Companion").hook {
            injectMember {
                method { name = "getVolteIcon" }
                beforeHook {
                    val position = args().last().cast<Int>() ?: return@beforeHook
                    val clazz =
                        "com.oplus.systemui.statusbar.policy.MobileIconSets".toClass(initialize = true)
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