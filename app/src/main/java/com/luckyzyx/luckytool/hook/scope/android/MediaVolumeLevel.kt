package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        val mediaVolumeLevel = prefs(ModulePrefs).getInt("media_volume_level", 0)
        val minVolumeZero = prefs(ModulePrefs).getBoolean("minimum_volume_level_can_be_zero", false)
        //Source AudioServiceExtImpl
        findClass("com.android.server.audio.AudioServiceExtImpl").hook {
            injectMember {
                method { name = "resetSystemVolume" }
                afterHook {
                    if (mediaVolumeLevel != 0) {
                        val maxField = if (SDK >= A13) "mMaxStreamVolume" else "MAX_STREAM_VOLUME"
                        val maxArray = field { name = maxField }.get(instance).cast<IntArray>()
                        maxArray?.set(3, mediaVolumeLevel)
                    }

                    if (minVolumeZero) {
                        val minField = if (SDK >= A13) "mMinStreamVolume" else "MIN_STREAM_VOLUME"
                        val minArray = field { name = minField }.get(instance).cast<IntArray>()
                        minArray?.forEachIndexed { index, i ->
                            if (i > 0) minArray[index] = 0
                        }
                    }
                }
            }
        }
    }
}