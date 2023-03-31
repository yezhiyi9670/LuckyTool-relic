package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.dp
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object FolderLayoutRowColume : YukiBaseHooker() {
    @Suppress("UNUSED_VARIABLE")
    override fun onHook() {
        val iconWidth = prefs(ModulePrefs).getInt("set_icon_width_in_folder", 80)
        //Source OplusDeviceProfile
        findClass("com.android.launcher3.OplusDeviceProfile").hook {
            injectMember {
                method {
                    name = "updateOplusFolderCellSize"
                    paramCount = 2
                }
                afterHook {
                    field {
                        name = "folderCellWidthPx"
                        superClass()
                    }.get(instance).set(iconWidth.dp)
                }
            }
        }
        //Source InvariantDeviceProfile
        findClass("com.android.launcher3.InvariantDeviceProfile").hook {
            injectMember {
                method {
                    name = "initGrid"
                    paramCount(3..4)
                }
                afterHook {
                    field {
                        name = "numFolderColumns"
                    }.get(instance).set(4)
                }
            }
        }
        if (SDK < A13) return
        //Source FolderGridOrganizer
        findClass("com.android.launcher3.folder.big.BigFolderGridOrganizer").hook {
            injectMember {
                method {
                    name = "calculateGridSize"
                }
                afterHook {
                    field {
                        name = "mCountX"
                        superClass()
                    }.get(instance).set(3)
                    field {
                        name = "mCountY"
                        superClass()
                    }.get(instance).set(3)
                }
            }
        }
    }
}