package com.luckyzyx.luckytool.hook.scope.android

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersionCode

object SystemEnableVolumeKeyControlFlashlight : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("enable_volume_key_control_flashlight", false)
        if (getOSVersionCode < 27) return
        //Source OplusScreenOffTorchHelper
        "com.android.server.power.OplusScreenOffTorchHelper".toClassOrNull()?.hook {
            injectMember {
                method { name = "getInstance";param(ContextClass) }
                afterHook {
                    if (!isEnable) return@afterHook
                    val context = args().first().cast<Context>() ?: return@afterHook
                    if (result == null) result = instanceClass.buildOf(context) {
                        param(ContextClass)
                    }
                }
            }
        }
    }
}