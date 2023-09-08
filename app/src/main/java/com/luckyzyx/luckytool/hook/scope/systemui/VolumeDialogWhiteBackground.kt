package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.graphics.drawable.LayerDrawable
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.DialogInterfaceClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.dp

object VolumeDialogWhiteBackground : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        var customAlpha =
            prefs(ModulePrefs).getInt("custom_volume_dialog_background_transparency", -1)
        dataChannel.wait<Int>("custom_volume_dialog_background_transparency") {
            customAlpha = it
        }
        //Source VolumeDialogImplEx
        VariousClass(
            "com.oplusos.systemui.volume.VolumeDialogImplEx", //C13
            "com.oplus.systemui.volume.OplusVolumeDialogImpl" //C14
        ).hook {
            injectMember {
                method { name = "isSurrealQualityOn" }
                afterHook {
                    if (customAlpha < 0) return@afterHook
                    resultFalse()
                }
            }
            injectMember {
                method { param(DialogInterfaceClass) }
                beforeHook {
                    if (customAlpha < 0) return@beforeHook
                    val value = customAlpha * 25
                    field { name = "mVerticalRowsLayerDrawable" }.get(instance)
                        .cast<LayerDrawable>()?.apply {
                            getDrawable(0)?.setBlurRadius(value.dp)
                            getDrawable(1)?.alpha = value
                        }
                    field { name = "mVolumeMoreLayerDrawable" }.get(instance).cast<LayerDrawable>()
                        ?.apply {
                            getDrawable(0)?.setBlurRadius(value.dp)
                            getDrawable(1)?.alpha = value
                        }
                    field { name = "mVolumeCaptionLayerDrawable" }.get(instance)
                        .cast<LayerDrawable>()
                        ?.apply {
                            getDrawable(0)?.setBlurRadius(value.dp)
                            getDrawable(1)?.alpha = value
                        }
                }
            }
        }
    }

    private fun Any.setBlurRadius(blurRadius: Int) {
        current().method {
            name = "setBlurRadius"
            paramCount = 1
        }.call(blurRadius)
    }
}