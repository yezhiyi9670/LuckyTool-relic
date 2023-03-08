package com.luckyzyx.luckytool.ui.application

import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import com.luckyzyx.luckytool.utils.tools.AppAnalyticsUtils

class MyApplication : ModuleApplication() {
    override fun onCreate() {
        super.onCreate()
        AppAnalyticsUtils.init(this)
    }
}


























