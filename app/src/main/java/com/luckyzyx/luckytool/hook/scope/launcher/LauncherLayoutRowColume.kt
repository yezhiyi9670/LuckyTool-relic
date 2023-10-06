package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.IntArrayType
import com.luckyzyx.luckytool.utils.ModulePrefs

object LauncherLayoutRowColume : YukiBaseHooker() {
    override fun onHook() {
        //Source UiConfig
        "com.android.launcher.UiConfig".toClass().apply {
            method { name = "isSupportLayout" }.hook {
                replaceToTrue()
            }
        }
        val maxRows = prefs(ModulePrefs).getInt("launcher_layout_max_rows", 6)
        val maxColumns = prefs(ModulePrefs).getInt("launcher_layout_max_columns", 4)
        //Source ToggleBarLayoutAdapter
        "com.android.launcher.togglebar.adapter.ToggleBarLayoutAdapter".toClass().apply {
            method { name = "initToggleBarLayoutConfigs" }.hook {
                before {
                    field {
                        name = "MIN_MAX_COLUMN"
                        type = IntArrayType
                    }.get().cast<IntArray>()?.set(1, maxColumns)
                    field {
                        name = "MIN_MAX_ROW"
                        type = IntArrayType
                    }.get().cast<IntArray>()?.set(1, maxRows)
                }
            }
        }
    }
}