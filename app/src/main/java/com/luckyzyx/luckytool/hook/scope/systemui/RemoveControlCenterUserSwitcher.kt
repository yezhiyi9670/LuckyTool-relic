package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object RemoveControlCenterUserSwitcher : YukiBaseHooker() {
    override fun onHook() {
        //Search Log showUserSwitcher
        findClass("com.oplusos.systemui.qs.OplusQSFooterImpl").hook {
            injectMember {
                method {
                    name = "showUserSwitcher"
                    emptyParam()
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}