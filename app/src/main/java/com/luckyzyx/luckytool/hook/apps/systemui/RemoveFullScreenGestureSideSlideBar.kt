package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveFullScreenGestureSideSlideBar : YukiBaseHooker() {
    override fun onHook() {
        //Source SideGestureViewManager
        findClass("com.oplusos.systemui.navigationbar.gesture.sidegesture.SideGestureViewManager").hook {
            injectMember {
                method {
                    name = "createView"
                }
                intercept()
            }
        }
    }
}