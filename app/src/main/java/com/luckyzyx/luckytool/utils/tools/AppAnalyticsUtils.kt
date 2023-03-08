package com.luckyzyx.luckytool.utils.tools

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

object AppAnalyticsUtils {

//    const val App_Center_Secret = BuildConfig.APP_CENTER_SECRET
//    const val App_Center_Secret = ""

    fun init(instance: Application) {
        if (!AppCenter.isConfigured()) {
            AppCenter.start(
                instance,
                "",
                Analytics::class.java,
                Crashes::class.java
            )
        }
    }
}