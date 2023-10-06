package com.luckyzyx.luckytool.hook.scope.otherapp

import android.app.Activity
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookAlphaBackupPro : YukiBaseHooker() {
    override fun onHook() {
        val isPro = prefs(ModulePrefs).getBoolean("remove_check_license", false)
        if (!isPro) return
        //Source HomeActivity
        "com.ruet_cse_1503050.ragib.appbackup.pro.activities.HomeActivity".toClass().apply {
            method { name = "onCreate" }.hook {
                before {
                    instance<Activity>().intent.putExtra("licenseState", "valid_licence")
                }
            }
        }
    }
}