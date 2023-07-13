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
        val clazz = "com.android.server.OplusScrollToTopRusHelper"
        if (clazz.toClass().hasMethod { name = "isInWhiteList" }.not()) return
        //Source OplusScrollToTopRusHelper -> OplusScrollToTopSystemManager
        findClass(clazz).hook {
            injectMember {
                method {
                    name = "isInWhiteList"
                    paramCount = 1
                }
                if (isEnable) replaceToTrue()
            }
        }
    }
}