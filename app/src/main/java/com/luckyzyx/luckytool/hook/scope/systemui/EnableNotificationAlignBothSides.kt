package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.ViewGroup
import androidx.core.view.*
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object EnableNotificationAlignBothSides : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Source NotificationPanelViewController
        findClass("com.android.systemui.statusbar.phone.NotificationPanelViewController").hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                }
                afterHook {
                    val view = field {
                        name = "mNotificationStackScroller"
                        superClass()
                    }.get(instance).cast<ViewGroup>() ?: return@afterHook
                    val qsPanelPadding = view.resources.getDimensionPixelSize(
                        view.resources.getIdentifier(
                            "qs_header_panel_side_padding", "dimen", packageName
                        )
                    )
                    view.setViewPadding(qsPanelPadding)
                }
            }
            injectMember {
                method {
                    name = "updateResources"
                }
                afterHook {
                    val view = field {
                        name = "mNotificationStackScroller"
                        superClass()
                    }.get(instance).cast<ViewGroup>() ?: return@afterHook
                    val qsPanelPadding = view.resources.getDimensionPixelSize(
                        view.resources.getIdentifier(
                            "qs_header_panel_side_padding", "dimen", packageName
                        )
                    )
                    view.setViewPadding(qsPanelPadding)
                }
            }
        }
    }

    private fun ViewGroup.setViewPadding(padding: Int) {
        val mConfiguration: Configuration = context.resources.configuration
        if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setPadding(
                padding - marginLeft,
                paddingTop,
                padding - marginRight,
                paddingBottom
            )
        } else setPadding(0)
    }
}