package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomMediaPlayerSupport : YukiBaseHooker() {
    override fun onHook() {
        val customList = prefs(ModulePrefs).getString("custom_media_player_support", "None")
        //Source MediaSessionHelper
        findClass("business.module.media.MediaSessionHelper").hook {
            injectMember {
                method {
                    modifiers { isStatic }
                    emptyParam()
                    returnType = ListClass
                }
                afterHook {
                    if (customList.isBlank() || customList == "None") return@afterHook
                    val list = result<List<String>>() ?: return@afterHook
                    result = list.toMutableList().apply {
                        if (customList.contains("\n")) {
                            customList.replace(" ", "").split("\n")
                                .forEach { if (it.isNotBlank()) add(it) }
                        } else add(customList)
                    }
                }
            }
        }
    }
}