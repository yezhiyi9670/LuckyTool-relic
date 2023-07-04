package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.getOSVersion

object RemoveBottomAppIconOfRecentTaskList : YukiBaseHooker() {
    override fun onHook() {
        //Source DockView
        findClass("com.oplus.quickstep.dock.DockView").hook {
            injectMember {
                method {
                    name = if (getOSVersion() >= 13.1) "toggleVisibility"
                    else "show"
                }
                afterHook { instance<ViewGroup>().isVisible = false }
            }
        }
    }
}