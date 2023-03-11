package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object RemoveLockScreenBottomButton : YukiBaseHooker() {
    override fun onHook() {
        val leftButton =
            prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_left_button", false)
        val rightButton =
            prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_right_camera", false)
        //Source KeyguardBottomAreaView
        findClass("com.android.systemui.statusbar.phone.KeyguardBottomAreaView").hook {
            injectMember {
                method {
                    name = "updateLeftAffordanceVisibility"
                }
                beforeHook {
                    if (!leftButton) return@beforeHook
                    field {
                        name = "mLeftAffordanceView"
                        superClass(isOnlySuperClass = true)
                    }.get(instance).cast<View>()?.isVisible = false
                    resultNull()
                }
            }
            injectMember {
                method {
                    name = "updateCameraVisibility"
                }
                beforeHook {
                    if (!rightButton) return@beforeHook
                    field {
                        name = "mRightAffordanceView"
                        superClass(isOnlySuperClass = true)
                    }.get(instance).cast<View>()?.isVisible = false
                    resultNull()
                }
            }
        }
    }
}