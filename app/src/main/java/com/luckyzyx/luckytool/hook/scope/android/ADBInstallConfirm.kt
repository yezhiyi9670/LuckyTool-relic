package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object ADBInstallConfirm : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_adb_install_confirm", false)
        //Source OplusPackageInstallInterceptManager
        VariousClass(
            "com.android.server.pm.ColorPackageInstallInterceptManager", //A11
            "com.android.server.pm.OplusPackageInstallInterceptManager"
        ).hook {
            injectMember {
                method { name = "allowInterceptAdbInstallInInstallStage" }
                if (isEnable) replaceToFalse()
            }
        }
    }
}