package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class Remove72HourPasswordVerification : YukiBaseHooker() {
    override fun onHook() {
        //Source LockSettingsStrongAuth -> StrongAuthTimeoutAlarmListener
        findClass("com.android.server.locksettings.LockSettingsStrongAuth\$StrongAuthTimeoutAlarmListener").hook {
            injectMember {
                method {
                    name = "onAlarm"
                }
                if (prefs(XposedPrefs).getBoolean("remove_72hour_password_verification", false)) intercept()
            }
        }
    }
}