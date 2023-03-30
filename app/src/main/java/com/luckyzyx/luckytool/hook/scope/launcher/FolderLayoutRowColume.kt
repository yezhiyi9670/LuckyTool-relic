package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
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
                    val width = field {
                        name = "folderCellWidthPx"
                        superClass()
                    }.get(instance)
                    val height = field {
                        name = "folderCellHeightPx"
                        superClass()
                    }.get(instance)
                    width.set(iconWidth.dp)
                }
            }
        }
        //Source FolderGridOrganizer
        findClass("com.android.launcher3.folder.FolderGridOrganizer").hook {
            injectMember {
                constructor {
                    paramCount = 1
                }
                afterHook {
                    val x = field {
                        name = "mMaxCountX"
                    }.get(instance)
                    val y = field {
                        name = "mMaxCountY"
                    }.get(instance)
                    val items = field {
                        name = "mMaxItemsPerPage"
                    }.get(instance)
                    x.set(4)
                    y.set(4)
                    items.set(x.int() * y.int())
                }
            }
        }
    }
}