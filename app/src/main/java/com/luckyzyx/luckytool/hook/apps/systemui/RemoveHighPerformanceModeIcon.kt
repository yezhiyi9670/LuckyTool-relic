package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveHighPerformanceModeIcon : YukiBaseHooker() {
    override fun onHook() {
        //Source PhoneStatusBarPolicyEx
        findClass("com.oplusos.systemui.statusbar.phone.PhoneStatusBarPolicyEx").hook {
            injectMember {
                method {
                    name = "updateHighPerformanceIcon"
                    emptyParam()
                }
                beforeHook {
                    field {
                        name = "highPerformanceMode"
                    }.get(instance).setFalse()
                }
            }
        }
    }
}