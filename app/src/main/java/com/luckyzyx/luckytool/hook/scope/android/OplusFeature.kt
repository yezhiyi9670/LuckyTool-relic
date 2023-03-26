package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object OplusFeature : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusFeatureConfigManager
        findClass("com.oplus.content.OplusFeatureConfigManager").hook {
            injectMember {
                method {
                    name = "hasFeature"
                    paramCount = 1
                }
                beforeHook {
//                    loggerD(msg = args().first().string())
                }
            }
        }
    }
}