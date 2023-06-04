package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object RemoveAccessDeviceLogDialog : YukiBaseHooker() {

    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("remove_access_device_log_dialog", false)
        if (SDK < A13) return
        //Source LogcatManagerService
        findClass("com.android.server.logcat.LogcatManagerService").hook {
            injectMember {
                method { name = "processNewLogAccessRequest" }
                beforeHook {
                    if (!isEnable) return@beforeHook
                    val client = args().first().any() ?: return@beforeHook
                    method { name = "onAccessApprovedForClient" }.get(instance).call(client)
                    resultNull()
                }
            }
        }
    }
}