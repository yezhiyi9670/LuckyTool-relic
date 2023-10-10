package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.content.res.Resources
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.utils.sysui.DependencyUtils
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.safeOfNull

object MediaPlayerPanel : YukiBaseHooker() {
    override fun onHook() {
        //媒体播放器显示模式
        val isPermanent = VariousClass(
            "com.oplusos.systemui.qs.OplusQSTileMediaContainer", //C13.1
            "com.oplus.systemui.qs.OplusQSTileMediaContainer" //C14
        ).toClass().hasMethod { name = "setMediaMode" }.not()

//        YLog.debug("isPermanent -> $isPermanent")

        if (isPermanent) loadHooker(MediaPlayerDisplayMode)
        else loadHooker(MediaPlayerDisplayModeC13)
        //强制开启媒体切换按钮
        if (prefs(ModulePrefs).getBoolean("force_enable_media_toggle_button", false)) {
            if (SDK == A13) loadHooker(ForceEnableMediaToggleButton)
        }
    }

    object MediaPlayerDisplayMode : YukiBaseHooker() {
        @SuppressLint("DiscouragedApi")
        override fun onHook() {
            var mode = prefs(ModulePrefs).getString("set_media_player_display_mode", "0")
            dataChannel.wait<String>("set_media_player_display_mode") { mode = it }

            //Source updateQsMediaPanelView
            VariousClass(
                "com.oplusos.systemui.qs.OplusQSTileMediaContainer", //C13.1
                "com.oplus.systemui.qs.OplusQSTileMediaContainer" //C14
            ).toClass().apply {
                method { name = "setListening" }.hook {
                    after {
                        method { name = "updateResources" }.get(instance).call()
                    }
                }
                method { name = "updateQsMediaPanelView" }.hook {
                    replaceUnit {
                        val status = when (mode) {
                            "1" -> 0
                            "2" -> 8
                            "3" -> if (getMediaData() == null) 8 else 0
                            else -> return@replaceUnit
                        }
                        val res = args().first().cast<Resources>() ?: return@replaceUnit
                        val bool = args().last().cast<Boolean>() ?: return@replaceUnit
                        val linear = field { name = "mQsMediaPanelContainer" }.get(instance)
                            .cast<LinearLayout>() ?: return@replaceUnit
                        val mTmpConstraintSet = field { name = "mTmpConstraintSet" }
                            .get(instance).any() ?: return@replaceUnit
                        val smallHeight = res.getIdentifier(
                            "oplus_qs_media_panel_height_smallspace",
                            "dimen", MediaPlayerDisplayMode.packageName
                        ).takeIf { it != 0 } ?: return@replaceUnit
                        val height = res.getIdentifier(
                            "oplus_qs_media_panel_height",
                            "dimen", MediaPlayerDisplayMode.packageName
                        ).takeIf { it != 0 } ?: return@replaceUnit
                        val heightSize = safeOfNull {
                            res.getDimensionPixelSize(if (bool) smallHeight else height)
                        } ?: return@replaceUnit
                        mTmpConstraintSet.setVisibilitySet(linear.id, status)
                        if (status == 0) mTmpConstraintSet.constrainHeightSet(
                            linear.id, heightSize
                        )
                    }
                }
                method { name = "updateQsSecondTileContainer" }.hook {
                    replaceUnit {
                        val isShow = when (mode) {
                            "1" -> true
                            "2" -> false
                            "3" -> getMediaData() != null
                            else -> return@replaceUnit
                        }
                        val res = args().first().cast<Resources>() ?: return@replaceUnit
                        val bool = args().last().cast<Boolean>() ?: return@replaceUnit
                        val linear = field { name = "mSecondTileContainer" }.get(instance)
                            .cast<LinearLayout>() ?: return@replaceUnit
                        val mTmpConstraintSet = field { name = "mTmpConstraintSet" }
                            .get(instance).any() ?: return@replaceUnit
                        val smallSideMargin = res.getIdentifier(
                            "qs_footer_hl_tile_side_margin_smallspace",
                            "dimen", MediaPlayerDisplayMode.packageName
                        ).takeIf { it != 0 } ?: return@replaceUnit
                        val sideMargin = res.getIdentifier(
                            "qs_footer_hl_tile_side_margin",
                            "dimen", MediaPlayerDisplayMode.packageName
                        ).takeIf { it != 0 } ?: return@replaceUnit
                        val sideSize = safeOfNull {
                            res.getDimensionPixelSize(if (bool) smallSideMargin else sideMargin)
                        } ?: return@replaceUnit
                        val guideLine = res.getIdentifier(
                            "guide_line", "id", MediaPlayerDisplayMode.packageName
                        ).takeIf { it != 0 } ?: return@replaceUnit
                        if (isShow) {
                            val firstTile = field { name = "mFirstTileContainer" }.get(instance)
                                .cast<LinearLayout>() ?: return@replaceUnit
                            val smallContainerMargin = res.getIdentifier(
                                "qs_footer_hl_tile_two_container_margin_top_smallspace",
                                "dimen", MediaPlayerDisplayMode.packageName
                            ).takeIf { it != 0 } ?: return@replaceUnit
                            val containerMargin = res.getIdentifier(
                                "qs_footer_hl_tile_two_container_margin_top",
                                "dimen", MediaPlayerDisplayMode.packageName
                            ).takeIf { it != 0 } ?: return@replaceUnit
                            val containerSize = safeOfNull {
                                res.getDimensionPixelSize(if (bool) smallContainerMargin else containerMargin)
                            } ?: return@replaceUnit
                            mTmpConstraintSet.connectSet(linear.id, 6, 0, 6, 0)
                            mTmpConstraintSet.connectSet(linear.id, 7, guideLine, 6, sideSize)
                            mTmpConstraintSet.connectSet(
                                linear.id, 3, firstTile.id, 4, containerSize
                            )
                        } else {
                            mTmpConstraintSet.connectSet(linear.id, 6, guideLine, 7, sideSize)
                            mTmpConstraintSet.connectSet(linear.id, 7, 0, 7, 0)
                            mTmpConstraintSet.connectSet(linear.id, 3, 0, 3, 0)
                        }
                    }
                }
            }
        }
    }

    object MediaPlayerDisplayModeC13 : YukiBaseHooker() {
        override fun onHook() {
            var mode = prefs(ModulePrefs).getString("set_media_player_display_mode", "0")
            dataChannel.wait<String>("set_media_player_display_mode") { mode = it }

            //Source OplusQsMediaCarouselController
            "com.oplus.systemui.qs.media.OplusQsMediaCarouselController".toClass().apply {
                method { name = "setCurrentMediaData" }.hook {
                    after {
                        val status = when (mode) {
                            "1" -> true
                            "2" -> false
                            else -> return@after
                        }
                        val mediaModeChangeListener = field { name = "mediaModeChangeListener" }
                            .get(instance).any() ?: return@after
                        mediaModeChangeListener.current().method { name = "onChanged" }.call(status)
                    }
                }
                method { name = "setMediaModeChangeListener" }.hook {
                    after {
                        val status = when (mode) {
                            "1" -> true
                            "2" -> false
                            else -> return@after
                        }
                        val mediaModeChangeListener = args().first().any() ?: return@after
                        mediaModeChangeListener.current().method { name = "onChanged" }.call(status)
                    }
                }
            }
        }
    }

    fun getMediaData(): Any? {
        val clazz = VariousClass(
            "com.oplus.systemui.qs.media.OplusQsMediaCarouselController\$MediaPlayerData",  //C13
            "com.oplus.systemui.media.OplusMediaControllerImpl\$MediaPlayerData"  //C14
        ).toClass()
        val mediaPlayerData = clazz.field { name = "INSTANCE" }.get().any() ?: return null
        val firstActiveMediaOrSortKey = mediaPlayerData.current().method {
            name = if (SDK >= A14) "getFirstActiveMediaSortKey" else "firstActiveMedia"
            emptyParam()
        }.call() ?: return null
        if (SDK >= A14) mediaPlayerData.current().method {
            name = "getMediaDataKey";paramCount = 1
        }.call(firstActiveMediaOrSortKey) ?: return null
        val getData = firstActiveMediaOrSortKey.current().method {
            name = "getData";emptyParam()
        }.call()
        return getData
    }

    fun Any.connectSet(startId: Int, startSide: Int, endId: Int, endSide: Int, margin: Int) {
        this.current().method {
            name = "connect"
            paramCount = 5
        }.call(startId, startSide, endId, endSide, margin)
    }

    fun Any.constrainHeightSet(viewId: Int, height: Int) {
        this.current().method {
            name = "constrainHeight"
            paramCount = 2
        }.call(viewId, height)
    }

    fun Any.setVisibilitySet(viewId: Int, visibility: Int) {
        this.current().method {
            name = "setVisibility"
            paramCount = 2
        }.call(viewId, visibility)
    }

    object ForceEnableMediaToggleButton : YukiBaseHooker() {
        override fun onHook() {
            //Source OplusQsMediaPanelView
            "com.oplus.systemui.qs.media.OplusQsMediaPanelView".toClass().apply {
                method { name = "bindMediaData" }.hook {
                    after {
                        args().first().any() ?: field { name = "mMediaOutputBtn" }.get(instance)
                            .cast<ImageButton>()?.setMediaOutputBtn()
                    }
                }
            }
            //Source OplusQsMediaOutputDialog
            "com.oplus.systemui.qs.media.OplusQsMediaOutputDialog".toClass().apply {
                method { name = "bindMediaView" }.hook {
                    after {
                        args().first().any() ?: field { name = "mMediaOutputBtn" }.get(instance)
                            .cast<ImageButton>()?.setMediaOutputBtn()
                    }
                }
            }
        }
    }

    private fun ImageButton.setMediaOutputBtn() {
        isVisible = true
        isEnabled = true
        setOnClickListener {
            val clazz = "com.android.systemui.media.dialog.MediaOutputDialogFactory".toClass()
            val mMediaOutputDialogFactory = DependencyUtils(appClassLoader).get(clazz)
            mMediaOutputDialogFactory?.current()?.method { name = "create";paramCount = 3 }
                ?.call("", true, null)
        }
    }
}