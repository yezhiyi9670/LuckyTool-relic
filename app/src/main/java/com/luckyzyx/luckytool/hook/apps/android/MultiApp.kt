package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class MultiApp : YukiBaseHooker() {
    override fun onHook() {
        var multiAppEnable = prefs(XposedPrefs).getBoolean("multi_app_enable", false)
        var enabledMulti = prefs(XposedPrefs).getStringSet("enabledMulti", HashSet()).toList()
        dataChannel.wait<Boolean>(key = "multi_app_enable") { multiAppEnable = it }
        dataChannel.wait<List<String>>(key = "enabledMulti") { enabledMulti = it }
        findClass("com.oplus.multiapp.OplusMultiAppConfig").hook {
            injectMember {
                method {
                    name = "getAllowedPkgList"
                    returnType = ListClass
                }
                beforeHook {
                    if (multiAppEnable) {
                        field {
                            name = "mAllowedPkgList"
                            type = ListClass
                        }.get(instance).set(enabledMulti)
                    }
                }
            }
        }
    }
}