package com.luckyzyx.luckytool.hook.hooker

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.hook.scope.systemui.DoubleClickLockScreen
import com.luckyzyx.luckytool.hook.scope.systemui.HookSystemUIFeature
import com.luckyzyx.luckytool.hook.scope.systemui.VibrateWhenOpeningTheStatusBar
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.dp

object HookStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //HookSystemUIFeature
        loadHooker(HookSystemUIFeature)

        //双击状态栏锁屏
        if (prefs(ModulePrefs).getBoolean("statusbar_double_click_lock_screen", false)) {
            loadHooker(DoubleClickLockScreen)
        }
        //打开状态栏时振动
        if (prefs(ModulePrefs).getBoolean("vibrate_when_opening_the_statusbar", false)) {
            loadHooker(VibrateWhenOpeningTheStatusBar)
        }


        val customAlpha = 5

        //Source ActivatableNotificationView
        findClass("com.android.systemui.statusbar.notification.row.ActivatableNotificationView").hook {
            injectMember {
                method { name = "updateBackground";paramCount = 1 }
                beforeHook { field { name = "mDimmed" }.get(instance).setTrue() }
                afterHook {
                    field { name = "mBackgroundDimmed" }.get(instance).cast<View>()?.alpha =
                        customAlpha / 10.0F
                    field { name = "mRegionalGaussBlurController" }.get(instance).any()?.current()
                        ?.method {
                            name = "blurRowDimmed"
                            paramCount = 2
                        }?.call(instance(), "update Background")
                }
            }
            injectMember {
                method { name = "initBlurDimmedBgIfNeed" }
                afterHook {
                    val value = customAlpha * 25
                    val mBlurBgDimmed =
                        field { name = "mBlurBgDimmed" }.get(instance).cast<Drawable>()
                    val context = instance<View>().context
                    val drawable = ResourcesCompat.getDrawable(
                        context.resources,
                        context.resources.getIdentifier(
                            "systemui_icon_volume_bg",
                            "drawable",
                            packageName
                        ),
                        null
                    )
                    val layerDrawable = LayerDrawable(arrayOf(mBlurBgDimmed, drawable))
                    field { name = "mBackgroundDimmed" }.get(instance).cast<View>()?.apply {
                        background = layerDrawable.apply {
                            getDrawable(0).setBlurRadius(value.dp)
                            getDrawable(1).alpha = value
                        }
                    }
                }
            }
            injectMember {
                method { name = "setBlurDimmedRadius" }
                afterHook {
                    val value = customAlpha * 25
                    val mBlurBgDimmed =
                        field { name = "mBlurBgDimmed" }.get(instance).cast<Drawable>()
                    val context = instance<View>().context
                    val drawable = ResourcesCompat.getDrawable(
                        context.resources,
                        context.resources.getIdentifier(
                            "systemui_icon_volume_bg",
                            "drawable",
                            packageName
                        ),
                        null
                    )
                    val layerDrawable = LayerDrawable(arrayOf(mBlurBgDimmed, drawable))
                    field { name = "mBackgroundDimmed" }.get(instance).cast<View>()?.apply {
                        background = layerDrawable.apply {
                            getDrawable(0).setBlurRadius(value.dp)
                            getDrawable(1).alpha = value
                        }
                    }
                }
            }
        }

        //Source RegionalGaussBlurController
        findClass("com.oplusos.util.blur.RegionalGaussBlurController").hook {
            injectMember {
                method { name = "blurRowDimmed";paramCount = 4 }
                beforeHook {
                    args(1).set(0)
                    args(2).setTrue()
                }
            }
        }
    }

    private fun Any.setBlurRadius(blurRadius: Int) {
        current().method {
            name = "setBlurRadius"
            paramCount = 1
        }.call(blurRadius)
    }
}
