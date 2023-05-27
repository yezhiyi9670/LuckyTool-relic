package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object RemovePasswordTimeoutVerification : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_72hour_password_verification", false)
        //Source LockSettingsStrongAuth -> StrongAuthTimeoutAlarmListener
        findClass("com.android.server.locksettings.LockSettingsStrongAuth").hook {
            injectMember {
                method {
                    name = "rescheduleStrongAuthTimeoutAlarm"
                    paramCount = 2
                }
                if (isEnable) intercept()
            }
        }
    }
}