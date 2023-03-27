package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object FixTileAlignBothSides : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        //Sourcee QuickStatusBarHeader
        //Search quick_qs_panel -> qs_header_panel_side_padding 24dp
        findClass("com.android.systemui.qs.QuickStatusBarHeader").hook {
            injectMember {
                method {
                    name = "updateHeadersPadding"
                }
                afterHook {
                    field {
                        name = "mHeaderQsPanel"
                    }.get(instance).cast<LinearLayout>()?.apply {
                        val qsHeaderPanelSidePadding = resources.getDimensionPixelSize(
                            resources.getIdentifier(
                                "qs_header_panel_side_padding", "dimen", packageName
                            )
                        )
                        setViewPadding(qsHeaderPanelSidePadding)
                    }
                }
            }
        }
        //Source QSFragmentHelper
        //Search expanded_qs_scroll_view -> qs_brightness_mirror_side_padding / qs_bottom_side_padding 24dp
        findClass("com.oplusos.systemui.qs.helper.QSFragmentHelper").hook {
            injectMember {
                method {
                    name = "updateQsState"
                }
                afterHook {
                    field {
                        name = "mQSPanelScrollView"
                    }.get(instance).cast<ScrollView>()?.apply {
                        val mConfiguration: Configuration = context.resources.configuration
                        if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            val qsBrightnessMirrorSidePadding = resources.getDimensionPixelSize(
                                resources.getIdentifier(
                                    "qs_brightness_mirror_side_padding", "dimen", packageName
                                )
                            )
                            setViewPadding(qsBrightnessMirrorSidePadding)
                        } else setViewPadding(0)
                    }
                }
            }
        }
    }

    private fun View.setViewPadding(leftAndRight: Int) {
        setPadding(
            leftAndRight,
            paddingTop,
            leftAndRight,
            paddingBottom
        )
    }
}