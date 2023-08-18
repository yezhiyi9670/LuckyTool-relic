package com.luckyzyx.luckytool.hook.scope.android

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object RemoveAppUninstallButtonBlackList : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_app_uninstall_button_blacklist", false)
        if (SDK < A13) return
        //Source OplusUninstallableConfigManager
        findClass("com.android.server.pm.OplusUninstallableConfigManager").hook {
            injectMember {
                method { name = "loadUninstallableConfig" }
                afterHook {
                    if (!isEnable) return@afterHook
                    val mHideUninstallIcon =
                        field { name = "mHideUninstallIcon" }.get(instance).any()
                    mHideUninstallIcon?.current()?.field { name = "mList" }?.set(ArraySet<String>())
                    val mHideUninstallIconSoft =
                        field { name = "mHideUninstallIconSoft" }.get(instance).any()
                    mHideUninstallIconSoft?.current()?.field { name = "mList" }
                        ?.set(ArraySet<String>())
                }
            }
        }
    }
}