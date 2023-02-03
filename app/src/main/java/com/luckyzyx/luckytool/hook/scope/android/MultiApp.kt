package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object MultiApp : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusMultiAppConfig
        findClass("com.oplus.multiapp.OplusMultiAppConfig").hook {
            injectMember {
                method {
                    name = "getAllowedPkgList"
                    returnType = ListClass
                }
                beforeHook {
                    val isEnable = prefs(ModulePrefs).getBoolean("multi_app_enable", false)
                    val enabledMulti = prefs(ModulePrefs).getStringSet("enabledMulti", HashSet()).toList()
                    if (isEnable) field {
                        name = "mAllowedPkgList"
                        type = ListClass
                    }.get(instance).set(enabledMulti)
                }
            }
        }
    }
}