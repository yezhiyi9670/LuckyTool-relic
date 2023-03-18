package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.otherapp.HookAlphaBackupPro
import com.luckyzyx.luckytool.hook.scope.otherapp.HookKsWeb
import com.luckyzyx.luckytool.hook.scope.otherapp.HookMoreAnime

object HookOtherApp : YukiBaseHooker() {
    override fun onHook() {
        //好多动漫
        if (packageName == "com.east2d.everyimage") loadHooker(HookMoreAnime)

        //Alpha Backup Pro
        if (packageName == "com.ruet_cse_1503050.ragib.appbackup.pro") loadHooker(HookAlphaBackupPro)

        //KSWEB
        if (packageName == "ru.kslabs.ksweb") loadHooker(HookKsWeb)

    }
}
