package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.oplusota.UnlockLocalUpgrade
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookOplusOta : YukiBaseHooker() {
    override fun onHook() {
        //解锁本地安装
        if (prefs(XposedPrefs).getBoolean("unlock_local_upgrade", false)) {
            loadHooker(UnlockLocalUpgrade())
        }

        //OTA
        //AppointmentActivity
    }
}