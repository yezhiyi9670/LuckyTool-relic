package com.luckyzyx.luckytool.hook.apps.systemui

import android.graphics.Color
import android.graphics.Paint
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.PaintClass

class RemoveFullScreenGestureSideSliderBackground : YukiBaseHooker() {
    override fun onHook() {
        //SideGestureNavView
        findClass("com.oplusos.systemui.navigationbar.gesture.sidegesture.SideGestureNavView").hook {
            injectMember {
                method {
                    name = "initPaint"
                }
                afterHook {
                    field {
                        name = "mBezierPaint"
                        type = PaintClass
                    }.get(instance).cast<Paint>()?.apply {
                        color = Color.TRANSPARENT
                        alpha = 0
                    }
                }
            }
        }
    }
}