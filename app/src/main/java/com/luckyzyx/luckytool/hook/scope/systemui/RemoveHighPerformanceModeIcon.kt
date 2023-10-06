package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method

object RemoveHighPerformanceModeIcon : YukiBaseHooker() {
    override fun onHook() {
        //Source PhoneStatusBarPolicyEx
        VariousClass(
            "com.oplusos.systemui.statusbar.phone.PhoneStatusBarPolicyEx",
            "com.oplus.systemui.statusbar.phone.OplusPhoneStatusBarPolicyExImpl" //C14
        ).toClass().apply {
            method {
                name = "updateHighPerformanceIcon"
                emptyParam()
            }.hook {
                before {
                    field { name = "highPerformanceMode" }.get(instance).setFalse()
                }
            }
        }
    }
}