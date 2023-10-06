package com.luckyzyx.luckytool.hook.scope.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.method

object AllowReplaceInstall : YukiBaseHooker() {
    override fun onHook() {
        //Search ->  currentVersionCode / apkVersioncode -> Method
        "com.android.packageinstaller.oplus.OPlusPackageInstallerActivity".toClass().apply {
            method { name = "parseReplaceInstall" }.hook {
                replaceUnit {
                    instance.current().method { name = "preSafeInstall" }.call()
                }
            }
        }
    }
}