package com.luckyzyx.luckytool.hook.scope.settings

import android.util.ArrayMap
import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.AnyClass
import com.highcapable.yukihookapi.hook.type.java.AtomicBooleanClass
import com.highcapable.yukihookapi.hook.type.java.InputStreamClass
import com.highcapable.yukihookapi.hook.type.java.MapClass
import com.highcapable.yukihookapi.hook.type.java.OutputStreamClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import java.io.Reader

object DarkModeList : YukiBaseHooker() {
    override fun onHook() {
        var supportlistSet = prefs(ModulePrefs).getStringSet("dark_mode_support_list", ArraySet())
        dataChannel.wait<Set<String>>("dark_mode_support_list") { supportlistSet = it }

        //Source DarkModeFileUtils
        searchClass {
            from(
                "qc",
                "oe",
                "re",
                "qe",
                "be",
                "te",
                "ue",
                "ae",
                "pe",
                "je",
                "ie",
                "oc",
                "ke"
            ).absolute()
            field { type = AnyClass }.count(2)
            field { type = AtomicBooleanClass }.count(1)
            field { type = MapClass }.count(1)
            method { param(Reader::class.java) }.count(1)
            method { param(InputStreamClass) }.count(1)
            method { param { it[0] == OutputStreamClass } }.count(1)
        }.get()?.hook {
            val objectName = instanceClass.classes[0]?.simpleName
            val darkModeData =
                (instanceClass.canonicalName!! + "\$$objectName").toClass()
            injectMember {
                method { param(Reader::class.java) }
                replaceUnit {
                    val supportListMap = ArrayMap<String, Int>()
                    supportlistSet.forEach {
                        if (it.contains("|")) {
                            val arr = it.split("|").toMutableList()
                            if (arr.size < 2 || arr[1].isBlank()) arr[1] = (0).toString()
                            supportListMap[arr[0]] = arr[1].toInt()
                        } else supportListMap[it] = 0
                    }
                    val dataMap = ArrayMap<String, Any>()
                    supportListMap.forEach {
                        if (it.value == 0) dataMap[it.key] = darkModeData.newInstance()
                        else {
                            dataMap[it.key] = darkModeData.buildOf(0L, 0, it.value, 0) {
                                paramCount = 4
                            }
                        }
                    }
                    field { type = MapClass }.get().set(dataMap.toMap())
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> DarkModeList")
    }
}