package com.luckyzyx.luckytool.hook.scope.directui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveTouchAppRecommendCard : YukiBaseHooker() {
    override fun onHook() {
        //Source DirectUIMainViewMode -> AppBean
        "com.coloros.directui.repository.datasource.AppBean".toClass().apply {
            method { name = "toCardUIInfo" }.hook {
                replaceTo(null)
            }
        }
    }
}