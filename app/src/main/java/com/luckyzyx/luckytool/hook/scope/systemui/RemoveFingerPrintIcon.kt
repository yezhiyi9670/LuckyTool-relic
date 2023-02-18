package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object RemoveFingerPrintIcon : YukiBaseHooker() {
    override fun onHook() {
        val isRemove = prefs(ModulePrefs).getBoolean("remove_fingerprint_icon", false)

        //Source OnScreenFingerprintUiMech
        VariousClass(
            "com.oplusos.systemui.keyguard.onscreenfingerprint.OnScreenFingerprintOpticalAnimCtrl",
            "com.oplus.systemui.keyguard.finger.onscreenfingerprint.OnScreenFingerprintUiMech"
        ).hook {
            injectMember {
                method {
                    name = "loadAnimDrawables"
                }
                beforeHook {
                    if (isRemove) resultNull()
                }
                afterHook {
//                    field { name = "mContext" }.get(instance).cast<Context>()?.injectModuleAppResources()
//                    field { name = "mFadeInAnimDrawable" }.get(instance).set(R.drawable.baseline_touch_app_24)
//                    field { name = "mFadeOutAnimDrawable" }.get(instance).set(R.drawable.baseline_touch_app_24)
//                    field { name = "mPressedAnimDrawable" }
                }
            }
        }
    }
}