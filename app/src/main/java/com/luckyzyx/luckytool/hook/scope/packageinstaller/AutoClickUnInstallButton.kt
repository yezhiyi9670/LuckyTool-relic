package com.luckyzyx.luckytool.hook.scope.packageinstaller

import android.widget.Button
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object AutoClickUnInstallButton : YukiBaseHooker() {
    override fun onHook() {
        //Source UninstallerActivity
        findClass("com.android.packageinstaller.UninstallerActivity").hook {
            injectMember {
                method {
                    name = "showUninstallConfirmation"
                    paramCount = 1
                }
                afterHook {
                    field {
                        name = "mUnInstallButton"
                    }.get(instance).cast<Button>()?.callOnClick()
                }
            }
        }
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.OPlusUninstallAppProgress").hook {
            injectMember {
                method {
                    name = "initView"
                }
                afterHook {
                    field {
                        name = "mOkButton"
                    }.get(instance).cast<Button>()?.callOnClick()
                }
            }
        }
    }
}