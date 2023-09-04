package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersionCode
import com.luckyzyx.luckytool.utils.getScreenOrientation
import com.luckyzyx.luckytool.utils.safeOfNull

object FixTileAlignBothSides : YukiBaseHooker() {
    override fun onHook() {
        if (getOSVersionCode <= 26) loadHooker(HookTileAlignVertical)
        loadHooker(HookTileAlignHorizontal)
    }

    private object HookTileAlignVertical : YukiBaseHooker() {
        @SuppressLint("DiscouragedApi")
        override fun onHook() {
            //Sourcee QuickStatusBarHeader 竖屏溢出
            //Search quick_qs_panel -> qs_header_panel_side_padding 24dp
            findClass("com.android.systemui.qs.QuickStatusBarHeader").hook {
                injectMember {
                    method { name = "updateHeadersPadding" }
                    afterHook {
                        field { name = "mHeaderQsPanel" }.get(instance).cast<LinearLayout>()
                            ?.apply {
                                val qsHeaderPanelSidePadding = safeOfNull {
                                    resources.getDimensionPixelSize(
                                        resources.getIdentifier(
                                            "qs_header_panel_side_padding", "dimen", packageName
                                        )
                                    )
                                } ?: return@afterHook
                                setViewPadding(qsHeaderPanelSidePadding)
                            }
                    }
                }
            }
        }
    }

    private object HookTileAlignHorizontal : YukiBaseHooker() {
        @SuppressLint("DiscouragedApi")
        override fun onHook() {
            val isCustomTile = prefs(ModulePrefs).getBoolean("control_center_tile_enable", false)
            val columnHorizontal = prefs(ModulePrefs).getInt("tile_columns_horizontal_c13", 4)

            //Source QSFragmentHelper 横屏溢出
            //Search expanded_qs_scroll_view -> qs_brightness_mirror_side_padding / qs_bottom_side_padding 24dp
            VariousClass(
                "com.oplusos.systemui.qs.helper.QSFragmentHelper", //C13
                "com.oplus.systemui.qs.helper.QSFragmentHelper" //C14
            ).hook {
                injectMember {
                    method { name = "updateQsState" }
                    afterHook {
                        field { name = "mQSPanelScrollView" }.get(instance).cast<ViewGroup>()
                            ?.apply {
                                getScreenOrientation(this) {
                                    if (it) setViewPadding(0)
                                    else {
                                        val qsBrightnessMirrorSidePadding =
                                            safeOfNull {
                                                resources.getDimensionPixelSize(
                                                    resources.getIdentifier(
                                                        "qs_brightness_mirror_side_padding",
                                                        "dimen",
                                                        packageName
                                                    )
                                                )
                                            } ?: return@getScreenOrientation
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
    }

    private fun View.setViewPadding(leftAndRight: Int) {
        setPadding(
            leftAndRight, paddingTop,
            leftAndRight, paddingBottom
        )
    }
}