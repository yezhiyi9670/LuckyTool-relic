package com.luckyzyx.luckytool.hook.scope.launcher

import android.widget.Button
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RecentTaskListClearButton : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusClearAllPanelView
        findClass("com.oplus.quickstep.views.OplusClearAllPanelView").hook {
            injectMember {
                method { name = "inflateIfNeeded" }
                afterHook {
                    field { name = "mClearAllBtn" }.get(instance).cast<Button>()?.isVisible = false
                }
            }
        }
    }
}