package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BitmapClass
import com.highcapable.yukihookapi.hook.type.android.PaintClass
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object FullScreenGestureSideSlideBar : YukiBaseHooker() {
    override fun onHook() {
        //Source SideGestureViewManager
        //Source SideGestureNavView
        val removeView = prefs(ModulePrefs).getBoolean("remove_side_slider", false)
        val removeBackground =
            prefs(ModulePrefs).getBoolean("remove_side_slider_black_background", false)
        val isReplaceWith = prefs(ModulePrefs).getBoolean("replace_side_slider_icon_switch", false)
        VariousClass(
            "com.oplusos.systemui.navbar.gesture.sidegesture.SideGestureNavView", //A11
            "com.oplusos.systemui.navigationbar.gesture.sidegesture.SideGestureNavView"
        ).hook {
            injectMember {
                method {
                    name = "onDraw"
                    paramCount = 1
                }
                if (removeView) intercept()
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
            injectMember {
                method {
                    name = "setBackIcon"
                    param(BitmapClass)
                }
                beforeHook {
                    if (!isReplaceWith) return@beforeHook
                    val leftPath =
                        prefs(ModulePrefs).getString("replace_side_slider_icon_on_left", "null")
                    val rightPath =
                        prefs(ModulePrefs).getString("replace_side_slider_icon_on_right", "null")
                    if (leftPath == "null" || rightPath == "null") return@beforeHook
                    val res = when (field { name = "mPosition" }.get(instance).int()) {
                        0 -> BitmapFactory.decodeFile(leftPath)
                        1 -> BitmapFactory.decodeFile(rightPath)
                        else -> return@beforeHook
                    }
                    if (res != null) args(0).set(res)
                }
            }
        }
    }
}