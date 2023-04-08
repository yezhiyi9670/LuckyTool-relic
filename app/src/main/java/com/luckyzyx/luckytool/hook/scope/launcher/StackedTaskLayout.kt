package com.luckyzyx.luckytool.hook.scope.launcher

import android.view.View
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.dp
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object StackedTaskLayout : YukiBaseHooker() {

    override fun onHook() {

        val isEnable = prefs(ModulePrefs).getBoolean("enable_stacked_task_layout", false)
        val level = prefs(ModulePrefs).getInt("set_task_stacking_level", 7)
        val isFix = prefs(ModulePrefs).getBoolean("fix_current_task_to_the_top", false)
        if (!isEnable) return
        //Source DeviceProfile -> overview_page_spacing
        findClass("com.android.launcher3.DeviceProfile").hook {
            injectMember {
                constructor {
                    paramCount(8..11)
                }
                afterHook {
                    if (SDK >= A13) field {
                        name = "overviewPageSpacing"
                    }.get(instance).set(-(level * 10).dp)
                }
            }
        }
        var oldView: View? = null
        //Source RecentsView
        findClass("com.android.quickstep.views.RecentsView").hook {
            if (SDK < A13) injectMember {
                method {
                    name = "setPageSpacing"
                    superClass()
                }
                beforeHook {
                    args().first().set(-(level * 10).dp)
                }
            }
            injectMember {
                method {
                    name = "applyLoadPlan"
                }
                beforeHook {
//                    args().first().apply {
//                        set(java.util.ArrayList(list<Any>().reversed()))
//                    }
                }
            }
            injectMember {
                method {
                    name = "notifyPageSwitchListener"
                }
                beforeHook {
                    if (!isFix) return@beforeHook
                    val count = method { name = "getTaskViewCount" }.get(instance).invoke<Int>()
                    if (count == null || count == 0) return@beforeHook
                    val view =
                        method { name = "getCurrentPageTaskView" }.get(instance).invoke<View>()
                            ?: return@beforeHook
                    view.z = 999f
                    if (oldView != view) {
                        oldView?.z = 0f
                        oldView = view
                    }
                }
            }
            injectMember {
                method {
                    name = "resetTaskVisuals"
                }
                afterHook {
                    if (!isFix) return@afterHook
                    val count = method { name = "getTaskViewCount" }.get(instance).invoke<Int>()
                    if (count == null || count == 0) return@afterHook
                    val view =
                        method { name = "getCurrentPageTaskView" }.get(instance).invoke<View>()
                            ?: return@afterHook
                    view.z = 999f
                    if (oldView != view) {
                        oldView?.z = 0f
                        oldView = view
                    }
                }
            }
        }
    }
}