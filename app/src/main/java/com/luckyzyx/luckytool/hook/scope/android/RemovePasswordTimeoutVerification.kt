package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object RemovePasswordTimeoutVerification : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_72hour_password_verification", false)

        //Source LockSettingsStrongAuth -> StrongAuthTimeoutAlarmListener
        "com.android.server.locksettings.LockSettingsStrongAuth".toClass().apply {
            method { name = "rescheduleStrongAuthTimeoutAlarm";paramCount = 2 }.hook {
                if (isEnable) intercept()
            }
        }
    }
}