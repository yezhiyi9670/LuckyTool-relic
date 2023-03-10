package com.luckyzyx.luckytool.hook.scope.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object AllowReplaceInstall : YukiBaseHooker() {
    override fun onHook() {
        //Search ->  currentVersionCode / apkVersioncode -> Method
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            injectMember {
                method {
                    name = "parseReplaceInstall"
                }
                replaceUnit {
                    method {
                        name = "preSafeInstall"
                    }.get(instance).call()
                }
            }
        }
    }
}