package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookSystemProperties : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemProperties
        val mediaVolumeLevel = prefs(ModulePrefs).getInt("media_volume_level", 0)
        findClass("android.os.SystemProperties").hook {
            injectMember {
                method {
                    name = "getInt"
                    returnType = IntType
                }
                beforeHook {
                    when (args(0).cast<String>()) {
                        "ro.config.media_vol_steps" -> {
                            if (mediaVolumeLevel != 0) result = mediaVolumeLevel
                        }
                    }
                }
            }
        }
    }
}