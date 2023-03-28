package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object FingerPrintIcon : YukiBaseHooker() {
    override fun onHook() {
        val removeMode = prefs(ModulePrefs).getString("remove_fingerprint_icon_mode", "0")
        val isReplaceIcon = prefs(ModulePrefs).getBoolean("replace_fingerprint_icon_switch", false)
        val iconPath = prefs(ModulePrefs).getString("replace_fingerprint_icon_path", "null")

        //Source OnScreenFingerprintUiMech
        VariousClass(
            "com.oplusos.systemui.keyguard.onscreenfingerprint.OnScreenFingerprintOpticalAnimCtrl",
            "com.oplus.systemui.keyguard.finger.onscreenfingerprint.OnScreenFingerprintUiMech"
        ).hook {
            injectMember {
                method {
                    name = "loadAnimDrawables"
                }
                when (removeMode) {
                    "0" -> if (isReplaceIcon) afterHook { instance.setDrawable(iconPath, true) }
                    "1" -> afterHook { instance.setDrawable(iconPath, false) }
                    "2" -> intercept()
                }
            }
            injectMember {
                method {
                    name = "startFadeInAnimation"
                }
                if (removeMode != "0") intercept() else if (isReplaceIcon) replaceUnit {
                    instance.setDrawable(iconPath, true)
                }
            }
            injectMember {
                method {
                    name = "startFadeOutAnimation"
                }
                if (removeMode != "0") intercept() else if (isReplaceIcon) intercept()
            }
//            injectMember {
//                method {
//                    name = "startToAnimInDream"
//                }
//                intercept()
//            }
            injectMember {
                method {
                    name = "startPressedAnimation"
                }
                if (removeMode == "2") intercept()
            }
        }
    }

    private fun Any.setDrawable(iconPath: String, isReplace: Boolean) {
        if (!isReplace) {
            current().field { name = "mFpIcon" }.cast<ImageView>()?.setImageDrawable(null)
            return
        }
        val mContext = current().field { name = "mContext" }.cast<Context>()
        val getCurrentUserContext =
            current().method { name = "getCurrentUserContext" }.invoke<Context>(mContext) ?: return
        val drawable =
            BitmapDrawable(getCurrentUserContext.resources, BitmapFactory.decodeFile(iconPath))
        //kgd_osfingerprint_icon
        current().field { name = "mImMobileDrawable" }.set(drawable)
        current().field { name = "mFpIcon" }.cast<ImageView>()?.setImageDrawable(drawable)
    }
}