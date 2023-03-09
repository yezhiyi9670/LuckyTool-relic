package com.luckyzyx.luckytool.ui.application

import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.utils.tools.AppAnalyticsUtils

class MyApplication : ModuleApplication() {
    private val isDebug = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()

        if (isDebug) return
        AppAnalyticsUtils.init(this)
        AppAnalyticsUtils.loadDeviceInfo()
        AppAnalyticsUtils.loadDeviceOTA()
    }
}


























