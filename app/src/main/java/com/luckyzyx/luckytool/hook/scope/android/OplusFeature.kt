package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD

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
                    loggerD(msg = args().first().string())
                }
            }
        }
    }
}