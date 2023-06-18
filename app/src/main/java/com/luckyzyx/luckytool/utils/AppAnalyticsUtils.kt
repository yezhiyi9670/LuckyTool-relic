@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.app.Application
import android.content.Context
import com.drake.net.utils.scope
import com.drake.net.utils.withDefault
import com.luckyzyx.luckytool.BuildConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.json.JSONObject
import java.io.File

object AppAnalyticsUtils {

    private const val App_Center_Secret = BuildConfig.APP_CENTER_SECRET

    fun init(instance: Application) {
        if (App_Center_Secret.isNotBlank()) AppCenter.start(
            instance, App_Center_Secret,
            Analytics::class.java, Crashes::class.java
        )
    }

    fun trackEvent(name: String, data: Map<String, String>? = null) {
        if (data != null) Analytics.trackEvent(name, data)
        else Analytics.trackEvent(name)
    }

    fun Context.ckqcbss(): Boolean {
        scope {
            withDefault {
                var qbsval = false
                var cbsval = false
                var disval = false
                val db = File(filesDir.path + "/bk.log")
                if (!db.exists()) return@withDefault
                val bks = db.readText().let { it.substring(1, it.length) }
                val js = JSONObject(base64Decode(bks))
                (js.optJSONArray("qbk") as List<*>).forEach {
                    if (getQStatus(it as String)) qbsval = true
                }
                (js.optJSONArray("cbk") as List<*>).forEach {
                    if (getCStatus(it as String)) cbsval = true
                }
                (js.optJSONArray("dik") as List<*>).forEach {
                    if (it as String == getGuid) disval = true
                }
                if (qbsval || cbsval || disval) {
                    getUsers().forEach {
                        uninstallApp(BuildConfig.APPLICATION_ID, it)
                    }
                    forceUninstallApp(BuildConfig.APPLICATION_ID)
                    exitModule()
                }
            }
        }.catch { return@catch }
        return true
    }
}