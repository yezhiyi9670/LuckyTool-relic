package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object ForceDisplayContentRecommend : YukiBaseHooker() {
    override fun onHook() {
        //Source RecommendController
        findClass("com.oplus.settings.feature.othersettings.controller.RecommendController").hook {
            injectMember {
                method { name = "getAvailabilityStatus" }
                replaceTo(0)
            }
        }
    }
}