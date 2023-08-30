package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersionCode

object ScreenColorTemperatureRGBPalette : YukiBaseHooker() {
    override fun onHook() {
        val isEnable =
            prefs(ModulePrefs).getBoolean("enable_screen_color_temperature_rgb_palette", false)
        if (getOSVersionCode < 27 || !isEnable) return
        //Source OplusRgbBallManager -> oplus.software.display.rgb_ball_support
        findClass("com.android.server.display.oplus.eyeprotect.manager.OplusRgbBallManager").hook {
            injectMember {
                constructor { emptyParam() }
                afterHook {
                    field { name = "mIsSupportColorModeRGB" }.get(instance).setTrue()
                    method { name = "initRGBValueAnimator" }.get(instance).call()
                }
            }
        }
    }
}