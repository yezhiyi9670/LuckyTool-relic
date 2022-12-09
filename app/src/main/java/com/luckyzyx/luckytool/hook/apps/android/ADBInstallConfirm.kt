package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class ADBInstallConfirm : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPackageInstallInterceptManager
        var isEnable = prefs(XposedPrefs).getBoolean("remove_adb_install_confirm", false)
        dataChannel.wait<Boolean>(key = "remove_adb_install_confirm") { isEnable = it }
        findClass("com.android.server.pm.OplusPackageInstallInterceptManager").hook {
            injectMember {
                method {
                    name = "allowInterceptAdbInstallInInstallStage"
                    paramCount = 5
                }
                if (isEnable) replaceToFalse()
            }
        }
    }
}