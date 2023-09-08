package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.ThemeUtils.isNightMode
import com.luckyzyx.luckytool.utils.getOSVersionCode
import com.luckyzyx.luckytool.utils.safeOfNan

@Suppress("unused")
object CustomNotificationBackgroundTransparency : YukiBaseHooker() {

    private var defaultNotifyPanelTintList: ColorStateList? = null
    private var defaultNotifyPanelElevation = -1f
    private var customAlpha = -1

    override fun onHook() {
        if (getOSVersionCode < 25) return
        customAlpha = prefs(ModulePrefs).getInt("custom_notification_background_transparency", -1)
        dataChannel.wait<Int>("custom_notification_background_transparency") {
            customAlpha = it
        }

        val isOld =
            "com.android.systemui.statusbar.notification.row.NotificationBackgroundView".toClassOrNull()
                ?.hasMethod {
                    name = "drawCustom"
                    paramCount = 2
                } ?: false

        //Source OplusNotificationBackgroundView
        findClass("com.oplusos.systemui.statusbar.notification.row.OplusNotificationBackgroundView").hook {
            injectMember {
                method { name = "drawRegionBlur";paramCount = 2 }.remedys {
                    method { name = "draw";paramCount = 2 }
                }
                beforeHook {
                    if (customAlpha < 0) return@beforeHook
                    modifyNotifyPanelAlpha(instance(), args().last().cast<Drawable>())
                }
            }
            injectMember {
                method { name = "draw";paramCount = 2;superClass() }
                beforeHook {
                    if (customAlpha < 0) return@beforeHook
                    modifyNotifyPanelAlpha(instance(), args().last().cast<Drawable>())
                }
            }
        }.by { isOld.not() }

        //Source NotificationBackgroundView
        findClass("com.android.systemui.statusbar.notification.row.NotificationBackgroundView").hook {
            injectMember {
                method { name = "draw";paramCount = 2 }
                beforeHook { modifyNotifyPanelAlpha(instance(), args().last().cast<Drawable>()) }
            }
            injectMember {
                method { name = "drawCustom";paramCount = 2 }
                beforeHook { modifyNotifyPanelAlpha(instance(), args().last().cast<Drawable>()) }
            }.ignoredNoSuchMemberFailure()
        }.by { isOld }

        //Source ExpandableNotificationRow
        findClass("com.android.systemui.statusbar.notification.row.ExpandableNotificationRow").hook {
            injectMember {
                method { name = "updateBackgroundForGroupState";emptyParam() }
                beforeHook {
                    if (customAlpha < 0) return@beforeHook
                    field { name = "mShowGroupBackgroundWhenExpanded" }.get(instance).setTrue()
                }
            }
        }
    }

    /**
     * 设置通知面板背景透明度
     * @param view 背景 View 实例
     * @param drawable 背景实例
     * @param isTint 是否着色 [view]
     */
    private fun modifyNotifyPanelAlpha(view: View?, drawable: Drawable?, isTint: Boolean = false) {
        if (view == null) return
        if (defaultNotifyPanelTintList == null) defaultNotifyPanelTintList = view.backgroundTintList
        if (defaultNotifyPanelElevation < 0f) defaultNotifyPanelElevation = view.elevation
        val currentColor = if (view.context.isNightMode) 0xFF404040.toInt() else 0xFFFAFAFA.toInt()
        when {

            isTint.not() && view.parent?.parent?.javaClass?.name?.contains("ChildrenContainer") == true -> {
                drawable?.alpha = 0
            }

            else -> {
                currentColor.colorAlphaOf(customAlpha / 10.0F).also {
                    if (isTint) view.backgroundTintList = ColorStateList.valueOf(it)
                    else drawable?.setTint(it)
                }
            }
        }
        view.elevation = if (customAlpha >= 0) 0f else defaultNotifyPanelElevation
    }

    /**
     * 调整颜色透明度
     * @param value 透明度
     * @return [Int] 调整后的颜色
     */
    private fun Int.colorAlphaOf(value: Float) =
        safeOfNan { (255.coerceAtMost(0.coerceAtLeast((value * 255).toInt())) shl 24) + (0x00ffffff and this) }

}