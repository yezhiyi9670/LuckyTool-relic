package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        val mediaVolumeLevel = prefs(ModulePrefs).getInt("media_volume_level", 0)
        //Source AudioServiceExtImpl
        findClass("com.android.server.audio.AudioServiceExtImpl").hook {
            injectMember {
                method { name = "resetSystemVolume" }
                afterHook {
                    if (mediaVolumeLevel == 0) return@afterHook
                    val fieldName = if (SDK >= A13) "mMaxStreamVolume" else "MAX_STREAM_VOLUME"
                    val arr = field { name = fieldName }.get(instance).cast<IntArray>()
                    arr?.set(3, mediaVolumeLevel)
                }
            }
        }
    }
}