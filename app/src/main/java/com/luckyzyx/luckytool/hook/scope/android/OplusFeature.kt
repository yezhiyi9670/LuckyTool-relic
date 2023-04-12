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
//                    when (args().first().string()) {
//                        //key KEY_MOTION_FLUENCY_OPTIMIZATION_SWITCH / iris5_motion_fluency_optimization_switch
//                        "oplus.software.video.rm_memc" -> resultFalse()
//                        "oplus.software.display.iris_enable" -> resultTrue()
//                        "oplus.software.display.memc_enable" -> resultTrue()
//                        "oplus.software.display.pixelworks_enable" -> resultTrue()
//                        "oplus.software.display.pixelworks_x7_enable" -> resultTrue()
//                    }
                }
            }
        }
    }
}