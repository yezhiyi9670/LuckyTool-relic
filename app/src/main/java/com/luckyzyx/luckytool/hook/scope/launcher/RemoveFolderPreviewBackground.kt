package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object RemoveFolderPreviewBackground : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusPreviewBackground folder_icon_bg big_folder_bg
        findClass("com.android.launcher3.folder.OplusPreviewBackground").hook {
            injectMember {
                method { name = "setup" }.all()
                afterHook { field { name = "mBgDrawable" }.get(instance).setNull() }
            }
            injectMember {
                method { name = "drawBackground" }
                intercept()
            }
        }
        if (SDK < A13) return
        //Source OplusFolderAnimationManager
        findClass("com.android.launcher3.folder.OplusFolderAnimationManager").hook {
            injectMember {
                method { name = "getFolderBackgroundAnimator" }
                intercept()
            }
        }
    }
}