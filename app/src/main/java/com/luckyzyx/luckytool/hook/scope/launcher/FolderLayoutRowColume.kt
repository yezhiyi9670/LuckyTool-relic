package com.luckyzyx.luckytool.hook.scope.launcher

import android.util.DisplayMetrics
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.DisplayMetricsClass
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object FolderLayoutRowColume : YukiBaseHooker() {
    override fun onHook() {
        val columns = prefs(ModulePrefs).getInt("set_icon_columns_in_folder", 3)
        //Source OplusDeviceProfile
        "com.android.launcher3.OplusDeviceProfile".toClass().apply {
            method {
                name = "updateOplusFolderCellSize"
                paramCount = 2
            }.hook {
                after {
                    val folderPageMarginLRDp = field {
                        name = "inv"
                        superClass()
                    }.get(instance).any()?.current()?.field {
                        name = "folderDisplayOption"
                    }?.any()?.current()?.field {
                        name = "folderPageMarginLRDp"
                    }?.float()
                    val metrics = field {
                        name = "mInfo"
                        superClass()
                    }.get(instance).any()?.current()?.field {
                        name = "metrics"
                    }?.cast<DisplayMetrics>()
                    val lrMargin = pxFromDp(folderPageMarginLRDp, metrics) ?: 0
                    val f = args().first().float()
                    val availableWidthPx = field {
                        name = "availableWidthPx"
                        superClass()
                    }.get(instance).int()
                    field {
                        name = "folderCellWidthPx"
                        superClass()
                    }.get(instance)
                        .set((((availableWidthPx - (lrMargin * 2)) / columns) * f).toInt())
                }
            }
        }
        //Source InvariantDeviceProfile
        "com.android.launcher3.InvariantDeviceProfile".toClass().apply {
            method {
                name = "initGrid"
                paramCount(3..4)
            }.hook {
                after {
                    field { name = "numFolderColumns" }.get(instance).set(columns)
                }
            }
        }
        if (SDK < A13) return
        //Source FolderGridOrganizer
        "com.android.launcher3.folder.big.BigFolderGridOrganizer".toClass().apply {
            method { name = "calculateGridSize" }.hook {
                after {
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

    private fun pxFromDp(float: Float?, displayMetrics: DisplayMetrics?): Int? {
        return "com.android.launcher3.ResourceUtils".toClass().method {
            name = "pxFromDp"
            param(FloatType, DisplayMetricsClass)
        }.get().invoke<Int>(float, displayMetrics)
    }
}