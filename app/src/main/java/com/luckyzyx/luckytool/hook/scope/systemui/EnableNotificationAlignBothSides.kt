package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.view.*
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.getScreenStatus
import kotlin.math.abs

object EnableNotificationAlignBothSides : YukiBaseHooker() {

    private var qsPanelPaddingPx: Int = 0

    override fun onHook() {
        //Source ExpandableNotificationRow
        findClass("com.oplusos.systemui.statusbar.notification.row.OplusExpandableNotificationRow").hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                    superClass()
                }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
            injectMember {
                method {
                    name = "onLayout"
                    superClass()
                }
                afterHook { instance<ViewGroup>().setViewWidth() }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun ViewGroup.setViewWidth() {
        if (qsPanelPaddingPx == 0) qsPanelPaddingPx = resources.getDimensionPixelSize(
            resources.getIdentifier("qs_header_panel_side_padding", "dimen", packageName)
        )
        getScreenStatus(resources) {
            layoutParams = ViewGroup.LayoutParams(layoutParams).apply {
                val left = abs(qsPanelPaddingPx - marginLeft)
                val right = abs(qsPanelPaddingPx - marginRight)
                width = if (it) {
                    resources.displayMetrics.widthPixels - left - right
                } else -1
            }
        }
    }
}