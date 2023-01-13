package com.luckyzyx.luckytool.hook.scope.camera

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object Enable10BitImageSupport : YukiBaseHooker() {
    override fun onHook() {
        //Title camera_heic_encode_10bits_title
        //Key OptionKey PRE_KEY_10BIT_HEIC_ENCODE pref_10bits_heic_encode_key
        //Source CameraConfig
        findClass("com.oplus.camera.configure.CameraConfig").hook {
            injectMember {
                method {
                    name = "getConfigBooleanValue"
                    paramCount = 1
                }
                beforeHook {
                    val key = args(0).cast<String>()
                    if (key == "com.oplus.10bits.heic.encode.support") resultTrue()
                }
            }
        }
    }
}