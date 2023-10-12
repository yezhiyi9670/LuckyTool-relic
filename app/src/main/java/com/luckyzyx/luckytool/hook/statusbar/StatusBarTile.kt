package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterTiles
import com.luckyzyx.luckytool.hook.scope.systemui.FixTileAlignBothSides
import com.luckyzyx.luckytool.hook.scope.systemui.LongPressTileOpenThePage
import com.luckyzyx.luckytool.hook.scope.systemui.MediaPlayerPanel
import com.luckyzyx.luckytool.hook.scope.systemui.RestorePageLayoutRowCountForEditTiles
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarTile : YukiBaseHooker() {
    override fun onHook() {
        //磁贴长按事件
        loadHooker(LongPressTileOpenThePage)

        //媒体播放器
        if (SDK >= A13) loadHooker(MediaPlayerPanel)

        //磁贴布局
        loadHooker(ControlCenterTiles)

        //磁贴两侧对齐
        if (prefs(ModulePrefs).getBoolean("fix_tile_align_both_sides", false)) {
            if (SDK >= A13) loadHooker(FixTileAlignBothSides)
        }
        //恢复磁贴编辑页面布局行数
        if (prefs(ModulePrefs).getBoolean("restore_page_layout_row_count_for_edit_tiles", false)) {
            if (SDK >= A13) loadHooker(RestorePageLayoutRowCountForEditTiles)
        }
    }
}