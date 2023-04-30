package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookSystemProperties : YukiBaseHooker() {
    override fun onHook() {
        val mediaVolumeLevel = prefs(ModulePrefs).getInt("media_volume_level", 0)
        //Source SystemProperties
        findClass("android.os.SystemProperties").hook {
            injectMember {
                method {
                    name = "getInt"
                    param(StringClass, IntType)
                    returnType = IntType
                }
                beforeHook {
                    when (args().first().string()) {
                        "ro.config.media_vol_steps" -> result = mediaVolumeLevel
                        "ro.config.media_vol_default" -> result = mediaVolumeLevel
                    }
                }
            }
        }
    }
}