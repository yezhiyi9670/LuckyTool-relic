package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object RemoveLockScreenBottomButton : YukiBaseHooker() {
    override fun onHook() {
        //Source KeyguardBottomAreaView
        findClass("com.android.systemui.statusbar.phone.KeyguardBottomAreaView").hook {
            injectMember {
                method {
                    name = "updateLeftAffordanceVisibility"
                }
                beforeHook {
                    if (prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_left_button",false)){
                        field {
                            name = "mLeftAffordanceView"
                            superClass(isOnlySuperClass = true)
                        }.get(instance).cast<View>()?.isVisible = false
                        resultNull()
                    }
                }
            }
            injectMember {
                method {
                    name = "updateCameraVisibility"
                }
                beforeHook {
                    if (prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_right_camera",false)){
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
}