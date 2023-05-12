package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterTilesColumn
import com.luckyzyx.luckytool.hook.scope.systemui.ControlCenterTilesColumnV13
import com.luckyzyx.luckytool.hook.scope.systemui.FixTileAlignBothSides
import com.luckyzyx.luckytool.hook.scope.systemui.LongPressTileOpenThePage
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarTile : YukiBaseHooker() {
    override fun onHook() {
        //磁贴长按事件
        loadHooker(LongPressTileOpenThePage)
        //磁贴行列数
        if (prefs(ModulePrefs).getBoolean("control_center_tile_enable", false)) {
            if (SDK >= A13) loadHooker(ControlCenterTilesColumnV13) else {
                loadHooker(ControlCenterTilesColumn)
            }
        }
        //磁贴两侧对齐
        if (prefs(ModulePrefs).getBoolean("fix_tile_align_both_sides", false)) {
            if (SDK >= A13) loadHooker(FixTileAlignBothSides)
        }
    }
}