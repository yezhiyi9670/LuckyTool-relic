package com.luckyzyx.luckytool.hook.scope.camera

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookCameraConfig : YukiBaseHooker() {
    override fun onHook() {

        val is10bit = prefs(ModulePrefs).getBoolean("enable_10_bit_image_support", false)
        val isHasselblad = prefs(ModulePrefs).getBoolean("enable_hasselblad_watermark_style", false)

        //Source CameraConfig
        VariousClass(
            "com.oplus.camera.aps.config.CameraConfig", //C12
            "com.oplus.camera.configure.CameraConfig" //C13
        ).hook {
            injectMember {
                method {
                    name = "getConfigBooleanValue"
                    paramCount = 1
                }
                beforeHook {
                    when (args(0).string()) {
                        //<string name="camera_heic_encode_10bits_title">10 亿色影像</string>
                        //OptionKey PRE_KEY_10BIT_HEIC_ENCODE pref_10bits_heic_encode_key
                        "com.oplus.10bits.heic.encode.support" -> if (is10bit) resultTrue()
                        "com.oplus.feature.video.10bit.support" -> if (is10bit) resultTrue()

                        //Source SloganUtil -> Shot on OnePlus / Hasselblad
                        "com.oplus.hasselblad.watermark.support.default" -> if (isHasselblad) resultTrue()
                        //通用哈苏水印
                        "com.oplus.camera.support.custom.hasselblad.watermark" -> if (isHasselblad) resultTrue()
                        //<string name="camera_hasselblad_watermark_setting_title_str">哈苏定制水印</string>
                        //OptionKey PRE_KEY_WATERMARK_HASSELBLAD / pref_hasselblad_watermark_function_key
                        "com.oplus.use.hasselblad.style.support" -> if (isHasselblad) resultTrue()
                        //<string name="camera_beauty_makeup_watermark_setting_title">美妆定制水印</string>
                        //OptionKey PRE_KEY_WATERMARK_MAKEUP pref_watermark_makeup_function_key
//                        "com.oplus.feature.custom.makeup.watermark.support" -> if (isHasselblad) resultTrue()
                    }
                }
            }
        }
    }
}