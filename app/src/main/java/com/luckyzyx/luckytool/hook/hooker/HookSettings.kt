package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.settings.RemoveTopAccountDisplay
import com.luckyzyx.luckytool.hook.scope.settings.ShowPackageNameInAppDetails
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookSettings : YukiBaseHooker() {
    override fun onHook() {
        //移除顶部账号显示
        if (prefs(ModulePrefs).getBoolean("remove_top_account_display", false)) {
            loadHooker(RemoveTopAccountDisplay)
        }
        //应用详情添加包名显示与复制
        if (prefs(ModulePrefs).getBoolean("show_package_name_in_app_details", false)) {
            loadHooker(ShowPackageNameInAppDetails)
        }
    }
}