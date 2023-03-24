package com.luckyzyx.luckytool.hook.scope.settings

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.view.View
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.PackageInfoClass
import com.luckyzyx.luckytool.utils.data.getAppVersion

object ShowPackageNameInAppDetails : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi", "SetTextI18n")
    override fun onHook() {
        //Source AppInfoFeature
        findClass("com.oplus.settings.feature.appmanager.AppInfoFeature").hook {
            injectMember {
                method {
                    name = "setAppLabelAndIcon"
                    paramCount = 1
                }
                afterHook {
                    val mRootView = field {
                        name = "mRootView"
                    }.get(instance).cast<View>() ?: return@afterHook
                    val appButtonsPreferenceController = args().first().any()!!
                    val instrumentedPreferenceFragment =
                        appButtonsPreferenceController.current().field {
                            name = "mFragment"
                        }.any()!!
                    val packageInfo = instrumentedPreferenceFragment.current().field {
                        type = PackageInfoClass
                    }.cast<PackageInfo>() ?: return@afterHook
                    val context = mRootView.context
                    val appSize = mRootView.findViewById<TextView?>(
                        context.resources.getIdentifier(
                            "app_size", "id", packageName
                        )
                    )
                    appSize.setTextIsSelectable(true)
                    val packName = packageInfo.packageName
                    val appVers = context.getAppVersion(packName)
                    if (appVers.size < 3) return@afterHook
                    val version =
                        if (appVers[2] == "null") "${appVers[0]}(${appVers[1]})" else "${appVers[0]}(${appVers[1]})_${appVers[2]}"
                    val versionText = context.getString(
                        context.resources.getIdentifier(
                            "version_text", "string", packageName
                        ), version
                    )
                    appSize.text = "$packName\n$versionText"
                }
            }
        }
    }
}