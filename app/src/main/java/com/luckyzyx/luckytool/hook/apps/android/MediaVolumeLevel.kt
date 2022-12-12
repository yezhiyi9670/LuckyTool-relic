package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        //Source AudioService
        val mediaVolumeLevel = prefs(XposedPrefs).getInt("media_volume_level", 0)
        if (mediaVolumeLevel == 0) return
        findClass("com.android.server.audio.AudioService").hook {
            injectMember {
                constructor {
                    paramCount = 5
                }
                afterHook {
                    field {
                        name = "MAX_STREAM_VOLUME"
                    }.get().cast<IntArray>()?.set(3, mediaVolumeLevel)
                }
            }
        }
    }
}