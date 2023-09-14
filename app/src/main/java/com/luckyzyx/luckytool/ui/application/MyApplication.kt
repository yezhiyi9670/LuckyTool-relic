package com.luckyzyx.luckytool.ui.application

import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.utils.AppAnalyticsUtils

class MyApplication : ModuleApplication() {
    override fun onCreate() {
        super.onCreate()
        val isBeta = BuildConfig.DEBUG || BuildConfig.VERSION_NAME.contains("beta")
        AppAnalyticsUtils.init(this, isBeta)
    }
}


























