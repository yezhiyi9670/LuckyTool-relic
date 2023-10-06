package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getScreenOrientation


object ControlCenterTilesColumn : YukiBaseHooker() {
    override fun onHook() {

        val columnUnexpandedVerticalC13 =
            prefs(ModulePrefs).getInt("tile_unexpanded_columns_vertical_c13", 5)
        val rowExpandedVerticalC13 = prefs(ModulePrefs).getInt("tile_expanded_rows_vertical_c13", 3)
        val columnExpandedVerticalC13 =
            prefs(ModulePrefs).getInt("tile_expanded_columns_vertical_c13", 4)
        val columnHorizontal = prefs(ModulePrefs).getInt("tile_columns_horizontal_c13", 4)

        //Source QuickQSPanel
        "com.android.systemui.qs.QuickQSPanel".toClass().apply {
            method { name = "getNumQuickTiles" }.hook {
                replaceTo(columnUnexpandedVerticalC13)
            }
        }

        //Source TileLayout
        "com.android.systemui.qs.TileLayout".toClass().apply {
            method { name = "updateResources" }.hook {
                after {
                    getScreenOrientation(instance<ViewGroup>()) {
                        if (it) field { name = "mMaxAllowedRows" }.get(instance)
                            .set(rowExpandedVerticalC13)
                    }
                }
            }
            method { name = "updateMaxRows" }.hook {
                before {
                    getScreenOrientation(instance<ViewGroup>()) {
                        if (it) field { name = "mRows" }.get(instance).set(rowExpandedVerticalC13)
                    }
                }
                after {
                    getScreenOrientation(instance<ViewGroup>()) {
                        if (it) {
                            field { name = "mRows" }.get(instance).set(rowExpandedVerticalC13)
                            resultTrue()
                        }
                    }
                }
            }
            method { name = "updateColumns" }.hook {
                after {
                    instance<ViewGroup>().apply {
                        getScreenOrientation(this) {
                            if (it) field { name = "mColumns" }.get(instance)
                                .set(columnExpandedVerticalC13)
                            else field { name = "mColumns" }.get(instance).set(columnHorizontal)
                        }
                        requestLayout()
                    }
                }
            }
        }
    }
}

object ControlCenterTilesColumnC12 : YukiBaseHooker() {
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
        "com.android.systemui.qs.QuickQSPanel".toClass().apply {
            method { name = "getNumQuickTiles" }.hook {
                after {
                    result = if (isVertical) {
                        columnUnexpandedVertical
                    } else {
                        columnUnexpandedHorizontal
                    }
                }
            }
        }

        //Source TileLayout
        "com.android.systemui.qs.TileLayout".toClass().apply {
            method { name = "updateColumns" }.hook {
                after {
                    instance<ViewGroup>().apply {
                        getScreenOrientation(context.resources) {
                            if (it) {
                                isVertical = true
                                field { name = "mColumns" }.get(instance)
                                    .set(columnExpandedVertical)
                            } else {
                                isVertical = false
                                field { name = "mColumns" }.get(instance)
                                    .set(columnExpandedHorizontal)
                            }
                        }
                        requestLayout()
                    }
                }
            }
        }
    }
}