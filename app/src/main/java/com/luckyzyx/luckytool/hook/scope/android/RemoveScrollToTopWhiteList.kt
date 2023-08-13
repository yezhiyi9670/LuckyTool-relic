package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object RemoveScrollToTopWhiteList : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_scroll_to_top_white_list", false)
        if (SDK < A13) return
        //Source OplusScrollToTopRusHelper -> OplusScrollToTopSystemManager
        findClass("com.android.server.OplusScrollToTopRusHelper").hook {
            if (instanceClass.hasMethod { name = "isInWhiteList" }) {
                injectMember {
                    method { name = "isInWhiteList";paramCount = 1 }
                    if (isEnable) replaceToTrue()
                }
            } else return@hook
        }
    }
}