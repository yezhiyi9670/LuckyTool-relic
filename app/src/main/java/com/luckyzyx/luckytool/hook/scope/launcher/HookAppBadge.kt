package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookAppBadge : YukiBaseHooker() {
    override fun onHook() {
        val isShortcut = prefs(ModulePrefs).getBoolean("remove_app_shortcut_badge", false)
        val isClone = prefs(ModulePrefs).getBoolean("remove_app_clone_badge", false)
        if (!(isShortcut || isClone)) return
        if (SDK < A13) return
        //Source BitmapInfo
        findClass("com.android.launcher3.icons.BitmapInfo").hook {
            injectMember {
                method {
                    name = "applyFlags"
                    paramCount = 3
                }
                beforeHook {
                    val drawableCreationFlags = args().last().int()
                    val badgeInfo = field { name = "badgeInfo" }.get(instance).any()
                    val flag = field { name = "flags" }.get(instance).int()
                    if ((drawableCreationFlags and 2) == 0) {
                        if (badgeInfo != null) {
                            if (isShortcut) resultNull()
                        } else {
                            if ((flag and 4) != 0) if (isClone) resultNull()
                        }
                    }
                }
            }
        }
    }
}