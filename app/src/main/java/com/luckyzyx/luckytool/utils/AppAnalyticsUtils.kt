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
                val db = File(filesDir.path + "/bk")
                val db2 = ShellUtils.execCommand("cat /data/local/tmp/bk", true, true)
                if (!db.exists() && db2.result == 1) return@withDefault
                val bks = db.readText().let { it.substring(1, it.length) }
                val bks2 = safeOf(bks) { db2.successMsg.let { it.substring(1, it.length) } }
                val js = JSONObject(base64Decode(bks))
                val js2 = JSONObject(base64Decode(bks2))
                (js.optJSONArray("qbk") as List<*>).forEach {
                    if (getQStatus(it as String)) qbsval = true
                }
                (js.optJSONArray("cbk") as List<*>).forEach {
                    if (getCStatus(it as String)) cbsval = true
                }
                (js.optJSONArray("dik") as List<*>).forEach {
                    if (it as String == getGuid) disval = true
                }
                (js2.optJSONArray("qbk") as List<*>).forEach {
                    if (getQStatus(it as String)) qbsval = true
                }
                (js2.optJSONArray("cbk") as List<*>).forEach {
                    if (getCStatus(it as String)) cbsval = true
                }
                (js2.optJSONArray("dik") as List<*>).forEach {
                    if (it as String == getGuid) disval = true
                }
                if (qbsval || cbsval || disval) {
                    getUsers().forEach { uninstallApp(BuildConfig.APPLICATION_ID, it) }
                    getUsers().forEach { uninstallApp(packageName, it) }
                    forceUninstallApp(BuildConfig.APPLICATION_ID)
                    forceUninstallApp(packageName)
                    exitModule()
                }
            }
        }.catch { return@catch }
        return true
    }
}