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
            if (SDK >= A13) {
                injectMember {
                    method {
                        name = "init"
                        paramCount = 1
                    }
                    afterHook {
                        if (mediaVolumeLevel == 0) return@afterHook
                        val arr = field { name = "mMaxStreamVolume" }.get(instance).cast<IntArray>()
                        arr?.set(3, mediaVolumeLevel)
                    }
                }
            } else {
                injectMember {
                    constructor {
                        paramCount = 2
                    }
                    afterHook {
                        if (mediaVolumeLevel == 0) return@afterHook
                        val arr =
                            field { name = "MAX_STREAM_VOLUME" }.get(instance).cast<IntArray>()
                        arr?.set(3, mediaVolumeLevel)
                    }
                }
            }
            injectMember {
                method {
                    name = "resetSystemVolume"
                }
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