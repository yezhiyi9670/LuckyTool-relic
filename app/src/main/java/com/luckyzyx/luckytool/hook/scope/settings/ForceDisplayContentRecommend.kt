package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object ForceDisplayContentRecommend : YukiBaseHooker() {
    override fun onHook() {
        //Source RecommendController
        "com.oplus.settings.feature.othersettings.controller.RecommendController".toClass().apply {
            method { name = "getAvailabilityStatus" }.hook {
                replaceTo(0)
            }
        }
    }
}