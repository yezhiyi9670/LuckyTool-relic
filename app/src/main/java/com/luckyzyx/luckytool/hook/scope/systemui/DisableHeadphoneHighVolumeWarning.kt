package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object DisableHeadphoneHighVolumeWarning : YukiBaseHooker() {
    override fun onHook() {
        //Sourcce VolumeDialogImplEx
        VariousClass(
            "com.oplusos.systemui.volume.VolumeDialogImplEx", //C13
            "com.android.systemui.volume.VolumeDialogImpl" //C14
        ).hook {
            injectMember {
                method { name = "showSafetyWarningH" }
                beforeHook {
                    val mContext = field { name = "mContext" }.get(instance).cast<Context>()
                        ?: return@beforeHook
                    val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) ?: return@beforeHook
                    audioManager.current().method { name = "disableSafeMediaVolume" }.call()
                    resultNull()
                }
            }
        }
    }
}