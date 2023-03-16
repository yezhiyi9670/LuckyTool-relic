@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import android.app.Application
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.BuildConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

@Obfuscate
object AppAnalyticsUtils {

    @Suppress("PrivatePropertyName")
    private val App_Center_Secret = BuildConfig.APP_CENTER_SECRET

    fun init(instance: Application) {
        if (App_Center_Secret.isNotBlank()) AppCenter.start(
            instance,
            App_Center_Secret,
            Analytics::class.java,
            Crashes::class.java
        )
    }

    fun trackEvent(name: String, data: Map<String, String>? = null) {
        if (data != null) Analytics.trackEvent(name, data)
        else Analytics.trackEvent(name)
    }
}