package com.luckyzyx.luckytool.hook.apps.systemui

import android.graphics.Color
import android.graphics.Paint
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.PaintClass
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class RemoveFullScreenGestureSideSlideBar : YukiBaseHooker() {
    override fun onHook() {
        //Source SideGestureViewManager
        //Source SideGestureNavView
        val removeIcon = prefs(XposedPrefs).getBoolean("remove_side_sliderbar_icon", false)
        val removeBackground = prefs(XposedPrefs).getBoolean("remove_side_sliderbar_black_background", false)
        VariousClass(
            "com.oplusos.systemui.navbar.gesture.sidegesture.SideGestureNavView", //A11
            "com.oplusos.systemui.navigationbar.gesture.sidegesture.SideGestureNavView"
        ).hook {
            injectMember {
                method {
                    name = "onDraw"
                    paramCount = 1
                }
                if (removeIcon) intercept()
            }
            injectMember {
                method {
                    name = "initPaint"
                    emptyParam()
                }
                afterHook {
                    if (!removeBackground) return@afterHook
                    field {
                        name = "mBezierPaint"
                        type = PaintClass
                    }.get(instance).cast<Paint>()?.color = Color.TRANSPARENT
                }
            }
        }
    }
}