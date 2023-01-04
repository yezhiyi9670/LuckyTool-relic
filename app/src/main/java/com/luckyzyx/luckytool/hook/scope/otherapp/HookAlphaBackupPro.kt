package com.luckyzyx.luckytool.hook.scope.otherapp

import android.app.Activity
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class HookAlphaBackupPro : YukiBaseHooker() {
    override fun onHook() {
        //移除许可证检测
        findClass("com.ruet_cse_1503050.ragib.appbackup.pro.activities.HomeActivity").hook {
            injectMember {
                method {
                    name = "onCreate"
                }
                beforeHook {
                    instance<Activity>().intent.putExtra("licenseState", "valid_licence")
                }
            }
        }
    }
}