package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.CanvasClass
import com.highcapable.yukihookapi.hook.type.android.MotionEventClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object PageIndicator : YukiBaseHooker() {
    override fun onHook() {
        val removeDesktop = prefs(ModulePrefs).getBoolean("remove_pagination_component", false)
        val removeFolder =
            prefs(ModulePrefs).getBoolean("remove_folder_pagination_component", false)
        val disableSliding =
            prefs(ModulePrefs).getBoolean("disable_pagination_component_sliding", false)
        //Source OplusPageIndicator
        findClass("com.android.launcher.pageindicators.OplusPageIndicator").hook {
            injectMember {
                method {
                    name = "onDraw"
                    param(CanvasClass)
                }
                beforeHook {
                    val view = instance<View>()
                    when (view.parent.javaClass.canonicalName) {
                        "com.android.launcher3.OplusDragLayer" -> if (removeDesktop) {
                            view.isVisible = false
                            resultNull()
                        }
                        "android.widget.FrameLayout" -> if (removeFolder) {
                            view.isVisible = false
                            resultNull()
                        }
                        else -> loggerD(msg = "$packageName\nError -> PageIndicator")
                    }
                }
            }
        }

        //Source PageIndicatorTouchHelper
        val cls = "com.android.launcher.pageindicators.PageIndicatorTouchHelper".hasClass()
        if (!cls) return
        findClass("com.android.launcher.pageindicators.PageIndicatorTouchHelper").hook {
            injectMember {
                method {
                    name = "dispatchTouchEvent"
                    param(MotionEventClass)
                }
                if (disableSliding) replaceToFalse()
            }
        }
    }
}