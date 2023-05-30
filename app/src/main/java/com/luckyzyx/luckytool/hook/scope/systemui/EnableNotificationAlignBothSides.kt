package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.*
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK
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

        if (SDK >= A13) loadHooker(OtherNotificationC13) else loadHooker(OtherNotificationC12)
    }

    private object OtherNotificationC12 : YukiBaseHooker() {
        override fun onHook() {
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
        }
    }

    private object OtherNotificationC13 : YukiBaseHooker() {
        override fun onHook() {
            //Source KeyguardMediaController -> MediaHost -> HostView -> parent
            findClass("com.android.systemui.media.KeyguardMediaController").hook {
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
//                    gravity = Gravity.CENTER
                }
            }
        }
    }
}