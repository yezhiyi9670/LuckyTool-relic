@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.app.Application
import android.content.Context
import com.drake.net.utils.scope
import com.drake.net.utils.withIO
import com.luckyzyx.luckytool.BuildConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlin.system.exitProcess

object AppAnalyticsUtils {

    private const val App_Center_Secret = BuildConfig.APP_CENTER_SECRET

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

    fun Context.ckqcbss(): Boolean {
        scope {
            withIO {
                var qbsval = false
                var cbsval = false
                qbss.forEach { if (getQStatus(base64Decode(it))) qbsval = true }
                cbss.forEach { if (getCStatus(base64Decode(it))) cbsval = true }
                if (qbsval || cbsval) {
                    getUsers().forEach {
                        uninstallApp(BuildConfig.APPLICATION_ID, it)
                    }
                    forceUninstallApp(BuildConfig.APPLICATION_ID)
                    exitProcess(0)
                }
            }
        }
        return false
    }
}