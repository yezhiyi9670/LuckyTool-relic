package com.luckyzyx.luckytool.hook.scope.systemui

import android.widget.ImageButton
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.hook.utils.sysui.DependencyUtils

object ForceEnableMediaToggleButton : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusQsMediaPanelView
        findClass("com.oplus.systemui.qs.media.OplusQsMediaPanelView").hook {
            injectMember {
                method { name = "bindMediaAction" }
                afterHook {
                    args().first().any() ?: field { name = "mMediaOutputBtn" }.get(instance)
                        .cast<ImageButton>()?.setMediaOutputBtn()
                }
            }
        }
        //Source OplusQsMediaOutputDialog
        findClass("com.oplus.systemui.qs.media.OplusQsMediaOutputDialog").hook {
            injectMember {
                method { name = "bindDevice" }
                afterHook {
                    args().first().any() ?: field { name = "mMediaOutputBtn" }.get(instance)
                        .cast<ImageButton>()?.setMediaOutputBtn()
                }
            }
        }
    }

    private fun ImageButton.setMediaOutputBtn() {
        isVisible = true
        isEnabled = true
        setOnClickListener {
            val clazz =
                "com.android.systemui.media.dialog.MediaOutputDialogFactory".toClass()
            val mMediaOutputDialogFactory =
                DependencyUtils(appClassLoader).get(clazz)
            mMediaOutputDialogFactory?.current()?.method {
                name = "create"
                paramCount = 3
            }?.call("", true, null)
        }
    }
}