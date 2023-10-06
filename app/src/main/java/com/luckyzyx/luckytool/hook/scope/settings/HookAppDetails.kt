package com.luckyzyx.luckytool.hook.scope.settings

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.PackageInfoClass
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.formatDate
import com.luckyzyx.luckytool.utils.getAppVersion
import com.luckyzyx.luckytool.utils.openMarketIntent

object HookAppDetails : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi", "SetTextI18n")
    override fun onHook() {
        val isPackName = prefs(ModulePrefs).getBoolean("show_package_name_in_app_details", false)
        val isLastUpdateTime =
            prefs(ModulePrefs).getBoolean("show_last_update_time_in_app_details", false)
        val isEnableCopy =
            prefs(ModulePrefs).getBoolean("enable_long_press_to_copy_in_app_details", false)
        val isIconMarket = prefs(ModulePrefs).getBoolean("click_icon_open_market_page", false)

        //Source AppInfoFeature
        "com.oplus.settings.feature.appmanager.AppInfoFeature".toClass().apply {
            method { name = "setAppLabelAndIcon";paramCount = 1 }.hook {
                after {
                    val mRootView = field { name = "mRootView" }.get(instance).cast<View>()
                        ?: return@after
                    val appButtonsPreferenceController = args().first().any() ?: return@after
                    val instrumentedPreferenceFragment =
                        appButtonsPreferenceController.current().field { name = "mFragment" }
                            .any() ?: return@after
                    val packageInfo = instrumentedPreferenceFragment.current().field {
                        type = PackageInfoClass
                    }.cast<PackageInfo>() ?: return@after
                    val context = mRootView.context
                    val appIcon = mRootView.findViewById<ImageView>(
                        context.resources.getIdentifier(
                            "app_icon", "id", this@HookAppDetails.packageName
                        )
                    )
                    val appSize = mRootView.findViewById<TextView>(
                        context.resources.getIdentifier(
                            "app_size", "id", this@HookAppDetails.packageName
                        )
                    )
                    val packName = packageInfo.packageName
                    val appVers = context.getAppVersion(packName, false)
                    if (appVers.size < 3) return@after
                    val version =
                        if (appVers[2] == "null") "${appVers[0]}(${appVers[1]})" else "${appVers[0]}(${appVers[1]})_${appVers[2]}"
                    val versionText = context.getString(
                        context.resources.getIdentifier(
                            "version_text", "string", this@HookAppDetails.packageName
                        ), version
                    )
                    context.injectModuleAppResources()
                    val updateStr = formatDate("YYYY/MM/dd HH:mm:ss", packageInfo.lastUpdateTime)
                    val updateTime =
                        if (isLastUpdateTime) "\n${context.getString(R.string.last_update_time)} $updateStr" else ""
                    if (isIconMarket) appIcon.setOnClickListener {
                        it.context.openMarketIntent(packName)
                    }
                    appSize.apply {
                        if (isEnableCopy) setTextIsSelectable(true)
                        if (isPackName) text = "$packName\n$versionText$updateTime"
                    }
                }
            }
        }
    }
}