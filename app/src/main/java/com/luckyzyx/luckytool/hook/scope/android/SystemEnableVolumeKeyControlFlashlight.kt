package com.luckyzyx.luckytool.hook.scope.android

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersionCode

object SystemEnableVolumeKeyControlFlashlight : YukiBaseHooker() {
    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean("enable_volume_key_control_flashlight", false)
        if (getOSVersionCode < 27) return

        //Source OplusScreenOffTorchHelper
        "com.android.server.power.OplusScreenOffTorchHelper".toClassOrNull()?.apply {
            method { name = "getInstance";param(ContextClass) }.hook {
                after {
                    if (!isEnable) return@after
                    val context = args().first().cast<Context>() ?: return@after
                    if (result == null) result = (this@apply).buildOf(context) {
                        param(ContextClass)
                    }
                }
            }
        }
    }
}