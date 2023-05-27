package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.*
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.getScreenOrientation

object EnableNotificationAlignBothSides : YukiBaseHooker() {

    override fun onHook() {
        //Source ExpandableNotificationRow
        findClass("com.android.systemui.statusbar.notification.row.ExpandableNotificationRow").hook {
            injectMember {
                method { name = "onFinishInflate" }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
            injectMember {
                method { name = "onLayout" }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
        }

        //Source MediaHost
        findClass("com.android.systemui.media.MediaHost").hook {
            injectMember {
                method { name = "getHostView" }
                afterHook {
                    val hostView = field { name = "hostView" }.get(instance).cast<ViewGroup>()
                        ?: return@afterHook
                    hostView.setMediaViewWidth()
                    result = hostView
                }
            }
        }

        val ubiquitousExpandableRow =
            "com.oplusos.systemui.statusbar.notification.row.UbiquitousExpandableRow"
        if (ubiquitousExpandableRow.toClassOrNull() == null) return
        //Source UbiquitousExpandableRow
        findClass(ubiquitousExpandableRow).hook {
            injectMember {
                method { name = "onFinishInflate" }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
            injectMember {
                method { name = "onLayout" }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun View.setViewWidth() {
        val qsPanelPaddingPx = resources.getDimensionPixelSize(
            resources.getIdentifier("qs_header_panel_side_padding", "dimen", packageName)
        )
        getScreenOrientation(this) {
            layoutParams = ViewGroup.LayoutParams(layoutParams).apply {
                width = if (it) {
                    resources.displayMetrics.widthPixels - (qsPanelPaddingPx * 2)
                } else -1
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun View.setMediaViewWidth() {
        val qsPanelPaddingPx = resources.getDimensionPixelSize(
            resources.getIdentifier("qs_header_panel_side_padding", "dimen", packageName)
        )
        getScreenOrientation(this) {
            if (it && width > 0) {
                layoutParams = FrameLayout.LayoutParams(layoutParams).apply {
                    width = resources.displayMetrics.widthPixels - (qsPanelPaddingPx * 2)
                    gravity = Gravity.CENTER
                }
            }
        }
    }
}