package com.luckyzyx.luckytool.hook.scope.android

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object MultiApp : YukiBaseHooker() {
    override fun onHook() {
        var isEnable = prefs(ModulePrefs).getBoolean("multi_app_enable", false)
        dataChannel.wait<Boolean>("multi_app_enable") { isEnable = it }
        var enabledMulti = prefs(ModulePrefs).getStringSet("multi_app_custom_list", ArraySet())
        dataChannel.wait<Set<String>>("multi_app_custom_list") { enabledMulti = it }
        //Source OplusMultiAppConfig
        findClass("com.oplus.multiapp.OplusMultiAppConfig").hook {
            injectMember {
                method {
                    name = "getAllowedPkgList"
                    returnType = ListClass
                }
                beforeHook {
                    if (!isEnable) return@beforeHook
                    result = enabledMulti.takeIf { e -> e.isNotEmpty() }?.toList() ?: return@beforeHook
                }
            }
        }
    }
}