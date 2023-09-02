package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterTilesColumn
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterTilesColumnC12
import com.luckyzyx.luckytool.hook.scope.systemui.FixTileAlignBothSides
import com.luckyzyx.luckytool.hook.scope.systemui.LongPressTileOpenThePage
import com.luckyzyx.luckytool.hook.scope.systemui.RestorePageLayoutRowCountForEditTiles
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarTile : YukiBaseHooker() {
    override fun onHook() {
        //磁贴长按事件
        loadHooker(LongPressTileOpenThePage)
        //磁贴行列数
        if (prefs(ModulePrefs).getBoolean("control_center_tile_enable", false)) {
            if (SDK >= A13) loadHooker(ControlCenterTilesColumn)
            else loadHooker(ControlCenterTilesColumnC12)
        }
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