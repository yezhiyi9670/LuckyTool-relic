package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.getScreenOrientation

object EnableNotificationAlignBothSides : YukiBaseHooker() {

    private var qsPanelPaddingPx = 0
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

        if (SDK >= A13) loadHooker(OtherNotification) else loadHooker(OtherNotificationC12)
    }

    private object OtherNotification : YukiBaseHooker() {
        override fun onHook() {
            //Source KeyguardMediaController -> MediaHost -> HostView -> parent
            VariousClass(
                "com.android.systemui.media.KeyguardMediaController", //C13
                "com.android.systemui.media.controls.ui.KeyguardMediaController" //C14
            ).hook {
                injectMember {
                    method { name = "setVisibility";paramCount = 2 }
                    beforeHook {
                        val viewGroup = args().first().cast<ViewGroup>() ?: return@beforeHook
                        val visible = args().last().cast<Int>() ?: return@beforeHook
                        val count = viewGroup.childCount
                        if ((visible == 0) && (count > 0)) {
                            if (viewGroup.width > 0) viewGroup.setViewWidth()
                        }
                    }
                }
            }

            val ubiquitousExpandableRow =
                "com.oplusos.systemui.statusbar.notification.row.UbiquitousExpandableRow"
            //Source UbiquitousExpandableRow
            ubiquitousExpandableRow.toClassOrNull()?.hook {
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
    }

    private object OtherNotificationC12 : YukiBaseHooker() {
        override fun onHook() {
            //Source OplusMediaHost
            findClass("com.oplusos.systemui.media.OplusMediaHost").hook {
                injectMember {
                    method { name = "updateViewVisibility" }
                    beforeHook {
                        val hostView = field {
                            name = "hostView";superClass()
                        }.get(instance).cast<ViewGroup>() ?: return@beforeHook
                        val visible = hostView.visibility
                        val count = hostView.childCount
                        if ((visible == 0) && (count > 0)) {
                            if (hostView.width > 0) getScreenOrientation(hostView) {
                                if (it) hostView.setViewWidth()
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun View.setViewWidth() {
        if (qsPanelPaddingPx == 0) qsPanelPaddingPx = resources.getDimensionPixelSize(
            resources.getIdentifier("qs_header_panel_side_padding", "dimen", packageName)
        )
        getScreenOrientation(this) {
            if (layoutParams != null) layoutParams = ViewGroup.LayoutParams(layoutParams).apply {
                width = if (it) resources.displayMetrics.widthPixels - (qsPanelPaddingPx * 2)
                else -1
            }
        }
    }
}