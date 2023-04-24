package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        //Source AudioService
        val mediaVolumeLevel = prefs(ModulePrefs).getInt("media_volume_level", 0)
        findClass("com.android.server.audio.AudioService").hook {
            injectMember {
                constructor {
                    paramCount(3..5)
                }
                afterHook {
                    if (mediaVolumeLevel == 0) return@afterHook
                    field {
                        name = "MAX_STREAM_VOLUME"
                    }.get().cast<IntArray>()?.set(3, mediaVolumeLevel)
                }
            }
        }
    }
}