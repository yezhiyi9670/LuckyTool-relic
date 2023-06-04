package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveBottomAppIconOfRecentTaskList : YukiBaseHooker() {
    override fun onHook() {
        //Source DockView
        findClass("com.oplus.quickstep.dock.DockView").hook {
            injectMember {
                method { name = "setVisibilityAlpha" }
                afterHook { instance<ViewGroup>().isVisible = false }
            }
        }
    }
}