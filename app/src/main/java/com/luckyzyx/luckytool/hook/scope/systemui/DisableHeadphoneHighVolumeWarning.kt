package com.luckyzyx.luckytool.hook.scope.systemui

import android.media.AudioManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object DisableHeadphoneHighVolumeWarning : YukiBaseHooker() {
    override fun onHook() {
        //Sourcce VolumeDialogImplEx
        findClass("com.oplusos.systemui.volume.VolumeDialogImplEx").hook {
            injectMember {
                method {
                    name = "showSafetyWarningH"
                    paramCount = 1
                }
                beforeHook {
                    field { name = "mAudioManager" }.get(instance).cast<AudioManager>()?.current()
                        ?.method { name = "disableSafeMediaVolume" }?.call()
                    resultNull()
                }
            }
        }
    }
}