package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.replaceSpace

object CustomMediaPlayerSupport : YukiBaseHooker() {
    override fun onHook() {
        val customList = prefs(ModulePrefs).getString("custom_media_player_support", "None")
        //Source MediaSessionHelper
        "business.module.media.MediaSessionHelper".toClass().apply {
            method {
                modifiers { isStatic }
                emptyParam()
                returnType = ListClass
            }.hook {
                after {
                    if (customList.isBlank() || customList == "None") return@after
                    val list = result<List<String>>() ?: return@after
                    result = list.toMutableList().apply {
                        val listString = customList.replaceSpace
                        if (listString.contains("\n")) {
                            listString.split("\n").forEach { if (it.isNotBlank()) add(it) }
                        } else add(customList)
                    }
                }
            }
        }
    }
}