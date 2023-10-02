package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object ScrollToTopWhiteList : YukiBaseHooker() {
    override fun onHook() {
        if (SDK < A13) return
        val mode = prefs(ModulePrefs).getString("set_click_statusbar_scroll_to_top_mode", "0")
        if (mode == "0") return
        //Source OplusScrollToTopRusHelper -> OplusScrollToTopSystemManager
        findClass("com.android.server.OplusScrollToTopRusHelper").hook {
            if (instanceClass.hasMethod { name = "isInWhiteList" }) {
                injectMember {
                    method { name = "isInWhiteList";paramCount = 1 }
                    beforeHook {
                        when (mode) {
                            "1" -> resultFalse()
                            "2" -> resultTrue()
                        }
                    }
                }
            } else return
        }
    }
}