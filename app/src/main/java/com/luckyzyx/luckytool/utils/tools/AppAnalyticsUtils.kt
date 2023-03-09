package com.luckyzyx.luckytool.utils.tools

import android.app.Application
import android.os.Build
import android.util.ArrayMap
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.utils.data.getColorOSVersion
import com.luckyzyx.luckytool.utils.data.getGuid
import com.luckyzyx.luckytool.utils.data.getProp
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

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

    fun loadDeviceInfo() {
        val deviceInfo = ArrayMap<String, String>()
        deviceInfo["Brand"] = Build.BRAND
        deviceInfo["Model"] = Build.MODEL
        deviceInfo["Product"] = Build.PRODUCT
        deviceInfo["Version"] =
            "${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})[$getColorOSVersion]"
        deviceInfo["Device"] = Build.DEVICE
        deviceInfo["Market"] = getProp("ro.vendor.oplus.market.name")
        deviceInfo["OTA_Version"] = getProp("ro.build.version.ota")
        trackEvent("DeviceInfo", deviceInfo.toMap())
    }

    fun loadDeviceOTA() {
        val ota = ArrayMap<String, String>()
        ota["OTA"] =
            "${Build.PRODUCT}_${getProp("ro.vendor.oplus.market.name")}_${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})[$getColorOSVersion]_${
                getProp("ro.build.version.ota")
            }_${getProp("ro.build.oplus_nv_id")}_${getGuid}"
        trackEvent("OTAInfo", ota.toMap())
    }
}