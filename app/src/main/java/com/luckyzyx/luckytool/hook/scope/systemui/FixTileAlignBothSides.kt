package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getScreenOrientation

object FixTileAlignBothSides : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        val isCustomTile = prefs(ModulePrefs).getBoolean("control_center_tile_enable", false)
        val columnHorizontal = prefs(ModulePrefs).getInt("tile_columns_horizontal_c13", 4)
        //Sourcee QuickStatusBarHeader 竖屏溢出
        //Search quick_qs_panel -> qs_header_panel_side_padding 24dp
        findClass("com.android.systemui.qs.QuickStatusBarHeader").hook {
            injectMember {
                method { name = "updateHeadersPadding" }
                afterHook {
                    field { name = "mHeaderQsPanel" }.get(instance).cast<LinearLayout>()?.apply {
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
        //Source QSFragmentHelper 横屏溢出
        //Search expanded_qs_scroll_view -> qs_brightness_mirror_side_padding / qs_bottom_side_padding 24dp
        findClass("com.oplusos.systemui.qs.helper.QSFragmentHelper").hook {
            injectMember {
                method { name = "updateQsState" }
                afterHook {
                    field { name = "mQSPanelScrollView" }.get(instance).cast<ViewGroup>()?.apply {
                        getScreenOrientation(this) {
                            if (it) setViewPadding(0) else {
                                val qsBrightnessMirrorSidePadding = resources.getDimensionPixelSize(
                                    resources.getIdentifier(
                                        "qs_brightness_mirror_side_padding", "dimen", packageName
                                    )
                                )
                                if (isCustomTile && columnHorizontal > 4) setViewPadding(
                                    qsBrightnessMirrorSidePadding
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun View.setViewPadding(leftAndRight: Int) {
        setPadding(
            leftAndRight, paddingTop,
            leftAndRight, paddingBottom
        )
    }
}