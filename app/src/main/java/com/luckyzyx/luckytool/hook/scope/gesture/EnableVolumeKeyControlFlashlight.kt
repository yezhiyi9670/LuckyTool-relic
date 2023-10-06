package com.luckyzyx.luckytool.hook.scope.gesture

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object EnableVolumeKeyControlFlashlight : YukiBaseHooker() {
    override fun onHook() {
        //<string name="Volume_key_control_flashlight">音量键开/关手电筒</string>
        //<string name="Volume_key_control_flashlight_summary_new">息屏时，长按音量下键开启或关闭手电筒。媒体播放时无法使用。</string>
        //Key Volume_down_button_longpress -> oplus.software.key_quickoperate_torch
        "com.oplus.gesture.util.GestureUtil".toClass().apply {
            method { name = "hasQuickOperateTorchFeature" }.hook {
                replaceToTrue()
            }
        }
    }
}