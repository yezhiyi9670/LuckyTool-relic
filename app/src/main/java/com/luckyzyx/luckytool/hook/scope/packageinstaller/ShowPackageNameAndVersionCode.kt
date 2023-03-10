package com.luckyzyx.luckytool.hook.scope.packageinstaller

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.method

object ShowPackageNameAndVersionCode : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Source ApkInfoView
        findClass("com.android.packageinstaller.oplus.view.ApkInfoView").hook {
            injectMember {
                method {
                    name = "loadApkInfo"
                }
                afterHook {
                    val context = field { name = "mContext" }.get(instance).cast<Context>()!!
                    val mAppVersion =
                        field { name = "mAppVersion" }.get(instance).cast<TextView>()?.apply {
                            layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                                width = LinearLayout.LayoutParams.MATCH_PARENT
                            }
                            isSingleLine = false
                        }
//                    val mAppSize = field { name = "mAppSize" }.get(instance).cast<TextView>()
                    (mAppVersion?.parent as LinearLayout).orientation = LinearLayout.VERTICAL
                    val versionStr = context.resources.getString(
                        context.resources.getIdentifier(
                            "app_info_version",
                            "string",
                            context.packageName
                        )
                    )
                    val apkInfo = args().first().any()!!
                    val packName = apkInfo.current().field { name = "packageName" }.string()
                    val versionName = apkInfo.current().field { name = "versionName" }.string()
                    val versionCode = apkInfo.current().field { name = "versionCode" }.int()
                    val oplusPackageUtil =
                        "com.android.packageinstaller.oplus.utils.OPlusPackageUtil".toClass()
                    val curVersionName = oplusPackageUtil.method {
                        name = "getPackageVersion"
                    }.get().invoke<String>(context, packName)
                    val curVersionCode = oplusPackageUtil.method {
                        name = "getTargetAppVersionCode"
                    }.get().invoke<Int>(context, packName)
                    val isNewInstall = oplusPackageUtil.method {
                        name = "isPackageInstalled"
                    }.get().invoke<Boolean>(context, packName)!!.not()
                    mAppVersion.text =
                        if (isNewInstall) "$packName\n$versionStr$versionName($versionCode)"
                        else "$packName\n$versionStr$curVersionName($curVersionCode) -> $versionName($versionCode)"
                }
            }
        }
    }
}