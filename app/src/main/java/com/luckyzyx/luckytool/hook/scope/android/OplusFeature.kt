package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.StringClass

object OplusFeature : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusFeatureConfigManager
        findClass("com.oplus.content.OplusFeatureConfigManager").hook {
            injectMember {
                method {
                    name = "hasFeature"
                    param(StringClass)
                    paramCount = 1
                }
                beforeHook {
//                    when (args(0).string()) {
//                        "oplus.software.support.gt.mode" -> resultTrue()
//                    }
                }
            }
        }
    }
}