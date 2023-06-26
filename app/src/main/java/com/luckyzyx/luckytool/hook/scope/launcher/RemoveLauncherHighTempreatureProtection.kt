package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveLauncherHighTempreatureProtection : YukiBaseHooker() {
    override fun onHook() {
        //Source HighTemperatureProtectionManager
        findClass("com.android.launcher.hightemperatureprotection.HighTemperatureProtectionManager").hook {
            injectMember {
                method { name = "isHighTempProtectedEnable" }
                replaceToFalse()
            }
            injectMember {
                method {
                    name = "isInterceptItemClickFromHighTempreatureProtection"
                    paramCount = 2
                }
                replaceToFalse()
            }
        }
    }
}