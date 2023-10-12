package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
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
        //媒体播放器模式
        var mediaMode = prefs(ModulePrefs).getString("set_media_player_display_mode", "0")
        dataChannel.wait<String>("set_media_player_display_mode_for_tile_rows") { mediaMode = it }
        //自动扩展
        var autoExpandTile =
            prefs(ModulePrefs).getBoolean("auto_expand_tile_rows_horizontal", false)
        dataChannel.wait<Boolean>("auto_expand_tile_rows_horizontal") { autoExpandTile = it }

        //Source QuickQSPanel
        "com.android.systemui.qs.QuickQSPanel".toClass().apply {
            method { name = "getNumQuickTiles" }.hook {
                replaceTo(columnUnexpandedVerticalC13)
            }
        }

        //Source TileLayout
        "com.android.systemui.qs.TileLayout".toClass().apply {
            method { name = "updateMaxRows" }.hook {
                before {
                    getScreenOrientation(instance<ViewGroup>()) {
                        val mRows = field { name = "mRows" }.get(instance).int()
                        val newRows = if (it) {
//                            if (MediaPlayerPanel.getMediaData() != null && mRows > 4) 4
//                            else rowExpandedVerticalC13
                            rowExpandedVerticalC13
                        } else {
                            if (autoExpandTile) {
                                when (mediaMode) {
                                    "2" -> 2
                                    "3" -> {
                                        if (MediaPlayerPanel.getMediaData() == null) 2
                                        else return@getScreenOrientation
                                    }

                                    else -> return@getScreenOrientation
                                }
                            } else return@getScreenOrientation
                        }
                        field { name = "mRows" }.get(instance).set(newRows)
                        result = mRows != newRows
                    }
                }
            }
            method { name = "updateColumns" }.hook {
                before {
                    instance<ViewGroup>().apply {
                        getScreenOrientation(this) {
                            val mColumns = field { name = "mColumns" }.get(instance).int()
                            val newColumns = if (it) columnExpandedVerticalC13
                            else columnHorizontal
                            field { name = "mColumns" }.get(instance).set(newColumns)
                            result = mColumns != newColumns
                        }
                    }
                }
            }
        }
    }
}

object ControlCenterTilesColumnC12 : YukiBaseHooker() {
    override fun onHook() {
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
                before {
                    getScreenOrientation(instance<View>()) {
                        result = if (it) columnUnexpandedVertical
                        else columnUnexpandedHorizontal
                    }
                }
            }
        }

        //Source TileLayout
        "com.android.systemui.qs.TileLayout".toClass().apply {
            method { name = "updateColumns" }.hook {
                before {
                    instance<ViewGroup>().apply {
                        getScreenOrientation(this) {
                            val mColumns = field { name = "mColumns" }.get(instance).int()
                            val newColumns = if (it) columnExpandedVertical
                            else columnExpandedHorizontal
                            field { name = "mColumns" }.get(instance).set(newColumns)
                            result = mColumns != newColumns
                        }
                    }
                }
            }
        }
    }
}