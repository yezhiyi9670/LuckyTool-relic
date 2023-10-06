package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
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
        "com.android.launcher.pageindicators.OplusPageIndicator".toClass().apply {
            method {
                name = "onDraw"
                param(CanvasClass)
            }.hook {
                before {
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

                        else -> YLog.debug(msg = "${PageIndicator.packageName}\nError -> PageIndicator")
                    }
                }
            }
        }

        //Source PageIndicatorTouchHelper
        val cls = "com.android.launcher.pageindicators.PageIndicatorTouchHelper".hasClass()
        if (!cls) return
        "com.android.launcher.pageindicators.PageIndicatorTouchHelper".toClass().apply {
            method {
                name = "dispatchTouchEvent"
                param(MotionEventClass)
            }.hook {
                if (disableSliding) replaceToFalse()
            }
        }
    }
}