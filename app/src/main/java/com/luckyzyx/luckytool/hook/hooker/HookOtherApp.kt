package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.otherapp.HookAlphaBackupPro
import com.luckyzyx.luckytool.hook.scope.otherapp.HookMoreAnime
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object HookOtherApp : YukiBaseHooker() {
    override fun onHook() {
        //好多动漫
        loadApp("com.east2d.everyimage", HookMoreAnime)

        //Alpha Backup Pro
        if (prefs(XposedPrefs).getBoolean("remove_check_license", false)) {
            loadApp("com.ruet_cse_1503050.ragib.appbackup.pro", HookAlphaBackupPro)
        }

    }
}
