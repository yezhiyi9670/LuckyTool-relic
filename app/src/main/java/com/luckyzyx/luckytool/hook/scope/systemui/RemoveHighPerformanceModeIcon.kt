package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveHighPerformanceModeIcon : YukiBaseHooker() {
    override fun onHook() {
        //Source PhoneStatusBarPolicyEx
        VariousClass(
            "com.oplusos.systemui.statusbar.phone.PhoneStatusBarPolicyEx",
            "com.oplus.systemui.statusbar.phone.OplusPhoneStatusBarPolicyExImpl" //C14
        ).hook {
            injectMember {
                method {
                    name = "updateHighPerformanceIcon"
                    emptyParam()
                }
                beforeHook {
                    field { name = "highPerformanceMode" }.get(instance).setFalse()
                }
            }
        }
    }
}