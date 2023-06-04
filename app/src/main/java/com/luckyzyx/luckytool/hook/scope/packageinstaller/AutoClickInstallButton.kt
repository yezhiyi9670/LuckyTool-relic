package com.luckyzyx.luckytool.hook.scope.packageinstaller

import android.widget.Button
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object AutoClickInstallButton : YukiBaseHooker() {
    override fun onHook() {
        //Source OPlusPackageInstallerActivity
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            injectMember {
                method { name = "startInstallConfirm" }
                afterHook {
                    field { name = "mOk" }.get(instance).cast<Button>()?.callOnClick()
                }
            }
        }
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.InstallAppProgress").hook {
            injectMember {
                method {
                    name = "onPackageInstalled"
                    paramCount = 1
                }
                afterHook {
                    if (args().first().int() == 0) field {
                        name = "mDoneButton"
                    }.get(instance).cast<Button>()?.callOnClick()
                }
            }
        }
    }
}