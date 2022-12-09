package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        //Source AudioService
        var isEnable = prefs(XposedPrefs).getInt("media_volume_level", 0)
        dataChannel.wait<Int>(key = "media_volume_level") { isEnable = it }
        findClass("com.android.server.audio.AudioService").hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook {
                    if (isEnable == 0) return@afterHook
                    field {
                        name = "MAX_STREAM_VOLUME"
                    }.get(instance).cast<IntArray>()?.set(3, isEnable)
                }
            }
        }
    }
}