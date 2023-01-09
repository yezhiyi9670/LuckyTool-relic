package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveLockScreenFingerPrintIcon : YukiBaseHooker() {
    override fun onHook() {
        //Source OnScreenFingerprintUiMech
        VariousClass(
            "com.oplusos.systemui.keyguard.onscreenfingerprint.OnScreenFingerprintOpticalAnimCtrl",
            "com.oplus.systemui.keyguard.finger.onscreenfingerprint.OnScreenFingerprintUiMech"
        ).hook {
            injectMember {
                method {
                    name = "loadAnimDrawables"
                }
                intercept()
            }
        }
    }
}