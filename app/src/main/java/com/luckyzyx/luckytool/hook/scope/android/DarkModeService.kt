package com.luckyzyx.luckytool.hook.scope.android

import android.util.ArrayMap
import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object DarkModeService : YukiBaseHooker() {
    override fun onHook() {
        var isEnable = prefs(ModulePrefs).getBoolean("dark_mode_list_enable", false)
        dataChannel.wait<Boolean>("dark_mode_list_enable") { isEnable = it }
        var supportlist = prefs(ModulePrefs).getStringSet("dark_mode_support_list", ArraySet())
        dataChannel.wait<Set<String>>("dark_mode_support_list") { supportlist = it }
        //Source OplusDarkModeData
        val darkModeData = "com.oplus.darkmode.OplusDarkModeData".toClass().newInstance()
        //Source OplusDarkModeServiceManager
        findClass("com.android.server.OplusDarkModeServiceManager").hook {
            injectMember {
                method {
                    name = "updateList"
                    paramCount = 1
                }
                afterHook {
                    if (!isEnable) return@afterHook
                    val newlist = ArrayMap<String, Any>()
                    supportlist.forEach { newlist[it] = darkModeData }
                    field {
                        name = "mRusAppMap"
                    }.get(instance).set(newlist.toMap())
//                    field {
//                        name = "mOpenApp"
//                    }.get(instance).set(supportlist)
//                    field {
//                        name = "mClickApp"
//                    }.get(instance).set(supportlist)
                }
            }
        }
    }
}