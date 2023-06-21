package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookWindowManagerService : YukiBaseHooker() {
    override fun onHook() {
        //移除DPI重启恢复
        val isDpi = prefs(ModulePrefs).getBoolean("remove_dpi_restart_recovery", false)
        //Source OplusWindowManagerService
        findClass("com.android.server.wm.OplusWindowManagerService").hook {
            injectMember {
                method {
                    name = "clearForcedDisplayDensityForUser"
                    paramCount = 2
                    superClass()
                }
                if (isDpi) intercept()
            }
        }
    }
}