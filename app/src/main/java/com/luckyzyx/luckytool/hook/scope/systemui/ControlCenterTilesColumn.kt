package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object ControlCenterTilesColumn : YukiBaseHooker() {
    override fun onHook() {

        var isVertical = true
        val columnUnexpandedVertical =
            prefs(ModulePrefs).getInt("tile_unexpanded_columns_vertical", 6)
        val columnUnexpandedHorizontal =
            prefs(ModulePrefs).getInt("tile_unexpanded_columns_horizontal", 6)
        val columnExpandedVertical = prefs(ModulePrefs).getInt("tile_expanded_columns_vertical", 4)
        val columnExpandedHorizontal =
            prefs(ModulePrefs).getInt("tile_expanded_columns_horizontal", 6)

        //Source QuickQSPanel
        findClass("com.android.systemui.qs.QuickQSPanel").hook {
            injectMember {
                method {
                    name = "getNumQuickTiles"
                }
                afterHook {
                    result = if (isVertical) {
                        columnUnexpandedVertical
                    } else {
                        columnUnexpandedHorizontal
                    }
                }
            }
        }

        //Source TileLayout
        findClass("com.android.systemui.qs.TileLayout").hook {
            injectMember {
                method {
                    name = "updateColumns"
                }
                afterHook {
                    instance<ViewGroup>().apply {
                        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            isVertical = true
                            field { name = "mColumns" }.get(instance).set(columnExpandedVertical)
                        } else {
                            isVertical = false
                            field { name = "mColumns" }.get(instance).set(columnExpandedHorizontal)
                        }
                        requestLayout()
                    }
                }
            }
        }
    }
}

object ControlCenterTilesColumnV13 : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {

        val columnUnexpandedVerticalC13 =
            prefs(ModulePrefs).getInt("tile_unexpanded_columns_vertical_c13", 5)
        val rowExpandedVerticalC13 = prefs(ModulePrefs).getInt("tile_expanded_rows_vertical_c13", 3)
        val columnExpandedVerticalC13 =
            prefs(ModulePrefs).getInt("tile_expanded_columns_vertical_c13", 4)
        val columnHorizontal = prefs(ModulePrefs).getInt("tile_columns_horizontal_c13", 4)
        val fixOverflow = prefs(ModulePrefs).getBoolean("fix_tile_align_both_sides", false)

        //Source QuickQSPanel
        findClass("com.android.systemui.qs.QuickQSPanel").hook {
            injectMember {
                method {
                    name = "getNumQuickTiles"
                }
                replaceTo(columnUnexpandedVerticalC13)
            }
        }

        //Source TileLayout
        findClass("com.android.systemui.qs.TileLayout").hook {
            injectMember {
                method {
                    name = "updateMaxRows"
                }
                beforeHook {
                    field { name = "mMaxAllowedRows" }.get(instance).set(rowExpandedVerticalC13)
                }
            }
            injectMember {
                method {
                    name = "updateColumns"
                }
                afterHook {
                    instance<ViewGroup>().apply {
                        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            field { name = "mColumns" }.get(instance).set(columnExpandedVerticalC13)
                        } else {
                            field { name = "mColumns" }.get(instance).set(columnHorizontal)
                        }
                        requestLayout()
                    }
                }
            }
        }

        if (!fixOverflow) return
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