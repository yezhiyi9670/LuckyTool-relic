package com.luckyzyx.luckytool.hook.scope.settings

import android.util.ArrayMap
import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.AnyClass
import com.highcapable.yukihookapi.hook.type.java.InputStreamClass
import com.highcapable.yukihookapi.hook.type.java.MapClass
import com.luckyzyx.luckytool.utils.tools.ModulePrefs
import java.io.Reader

object DarkModeList : YukiBaseHooker() {
    override fun onHook() {
        var supportlist = prefs(ModulePrefs).getStringSet("dark_mode_support_list", ArraySet())
        dataChannel.wait<Set<String>>("dark_mode_support_list") { supportlist = it }
        //Source DarkModeFileUtils
        searchClass {
            from("qc", "oe", "re", "qe", "be", "te").absolute()
            field {
                type = AnyClass
            }.count(2)
            field {
                type = MapClass
            }.count(1)
            method {
                param(Reader::class.java)
                paramCount = 1
            }.count(1)
            method {
                param(InputStreamClass)
                paramCount = 1
            }.count(1)
        }.get()?.hook {
            val objectName = instanceClass.classes[0]?.simpleName
            val darkModeData =
                (instanceClass.canonicalName!! + "\$$objectName").toClass().newInstance()
            injectMember {
                method {
                    param(Reader::class.java)
                    paramCount = 1
                }
                replaceUnit {
                    val newlist = ArrayMap<String, Any>()
                    supportlist.forEach { newlist[it] = darkModeData }
                    field { type = MapClass }.get().set(newlist.toMap())
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> DarkModeList")
    }
}