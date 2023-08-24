package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.highcapable.yukihookapi.hook.type.android.DrawableClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.closeScreen
import com.luckyzyx.luckytool.utils.safeOfNull

object LockScreenBottomButton : YukiBaseHooker() {
    override fun onHook() {
        //affordance_magazine
        var leftButton =
            prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_left_button", false)
        dataChannel.wait<Boolean>("remove_lock_screen_bottom_left_button") { leftButton = it }
        //affordance_camera
        var rightButton =
            prefs(ModulePrefs).getBoolean("remove_lock_screen_bottom_right_camera", false)
        dataChannel.wait<Boolean>("remove_lock_screen_bottom_right_camera") { rightButton = it }

        //affordance_flashlight
        var useFlashLight = prefs(ModulePrefs).getBoolean(
            "lock_screen_bottom_left_button_replace_with_flashlight", false
        )
        dataChannel.wait<Boolean>("lock_screen_bottom_left_button_replace_with_flashlight") {
            useFlashLight = it
        }
        var autoCloseScreen =
            prefs(ModulePrefs).getBoolean("lock_screen_switch_flashlight_auto_close_screen", false)
        dataChannel.wait<Boolean>("lock_screen_switch_flashlight_auto_close_screen") {
            autoCloseScreen = it
        }

        //Source KeyguardBottomAreaView
        findClass("com.android.systemui.statusbar.phone.KeyguardBottomAreaView").hook {
            injectMember {
                method { name = "onFinishInflate" }
                beforeHook {
                    if (!useFlashLight) return@beforeHook
                    instance<ViewGroup>().context.injectModuleAppResources()
                }
            }
            injectMember {
                method { name = "updateLeftAffordanceIcon" }
                afterHook {
                    if (!useFlashLight) return@afterHook
                    val context = instance<ViewGroup>().context
                    method { name = "updateLeftAffordanceVisibility" }.get(instance).call()
                    val mFlashlightController =
                        field { name = "mFlashlightController" }.get(instance).any()
                    val isEnable = mFlashlightController?.current()?.method { name = "isEnabled" }
                        ?.invoke<Boolean>() ?: false
                    val resId = if (isEnable) R.drawable.affordance_flashlight_on
                    else R.drawable.affordance_flashlight
                    val drawable = safeOfNull { ResourcesCompat.getDrawable(context.resources, resId, null) }
                    field { name = "mLeftAffordanceView";superClass() }.get(instance).any()
                        ?.current()?.method {
                            name = "setImageDrawable"
                            param(DrawableClass, BooleanType)
                            superClass()
                        }?.call(drawable, !isEnable)
                }
            }
            injectMember {
                method { name = "updateLeftAffordanceVisibility" }
                afterHook {
                    if (leftButton) {
                        field { name = "mLeftAffordanceView";superClass() }.get(instance)
                            .cast<View>()?.isVisible = false
                        return@afterHook
                    }
                    if (useFlashLight) {
                        field { name = "mLeftAffordanceView";superClass() }.get(instance)
                            .cast<ImageView>()?.isVisible = true
                    }
                }
            }
            injectMember {
                method { name = "launchLeftAffordance" }
                beforeHook {
                    if (!useFlashLight) return@beforeHook
                    method { name = "baseLaunchLeftAffordance" }.get(instance).call()
                    val mFlashlightController =
                        field { name = "mFlashlightController" }.get(instance).any()
                    val isEnable = mFlashlightController?.current()?.method { name = "isEnabled" }
                        ?.invoke<Boolean>() ?: true
                    mFlashlightController?.setFlashlight(!isEnable)
                    method { name = "updateLeftAffordanceIcon" }.get(instance).call()
                    if (autoCloseScreen) closeScreen(instance<ViewGroup>().context)
                    resultNull()
                }
            }
            injectMember {
                method { name = "updateCameraVisibility" }
                beforeHook {
                    if (!rightButton) return@beforeHook
                    field { name = "mRightAffordanceView";superClass() }.get(instance)
                        .cast<ImageView>()?.isVisible = false
                    resultNull()
                }
            }
        }
    }

    private fun Any.setFlashlight(status: Boolean) {
        this.current().method { name = "setFlashlight" }.call(status)
    }
}