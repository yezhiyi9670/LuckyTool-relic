package com.luckyzyx.luckytool.hook.scope.android

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object ZoomWindow : YukiBaseHooker() {
    override fun onHook() {
        var isEnable = prefs(ModulePrefs).getBoolean("enable_zoom_window", false)
        dataChannel.wait<Boolean>("enable_zoom_window") { isEnable = it }
        var supportList = prefs(ModulePrefs).getStringSet("zoom_window_support_list", ArraySet())
        dataChannel.wait<Set<String>>("zoom_window_support_list") { supportList = it }
        //Source OplusZoomWindowManagerService
        findClass("com.android.server.wm.OplusZoomWindowManagerService").hook {
            injectMember {
                method {
                    name = "isSupportZoomMode"
                    param(StringClass, IntType, StringClass, BundleClass)
                    paramCount = 4
                }
                beforeHook {
                    if (!isEnable) return@beforeHook
                    val target = args(0).string()
                    val packName = if (target.contains("/")) {
                        target.split("/").takeIf { e -> e.isNotEmpty() }?.get(0)
                            ?: return@beforeHook
                    } else target
                    if (supportList.contains(packName)) resultTrue()
                }
            }
        }
    }
}