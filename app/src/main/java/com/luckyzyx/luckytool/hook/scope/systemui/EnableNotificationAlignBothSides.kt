package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.ViewGroup
import androidx.core.view.*
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object EnableNotificationAlignBothSides : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Source ExpandableNotificationRow
        findClass("com.android.systemui.statusbar.notification.row.ExpandableNotificationRow").hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
            injectMember {
                method {
                    name = "onConfigurationChanged"
                    paramCount = 1
                }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun ViewGroup.setViewWidth() {
        val qsPanelPaddingPx = resources.getDimensionPixelSize(
            resources.getIdentifier("qs_header_panel_side_padding", "dimen", packageName)
        )
        val mConfiguration: Configuration = resources.configuration
        layoutParams = ViewGroup.LayoutParams(layoutParams).apply {
            val left = qsPanelPaddingPx - marginLeft
            val right = qsPanelPaddingPx - marginRight
            width = if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                resources.displayMetrics.widthPixels - left - right
            } else -1
        }
    }
}