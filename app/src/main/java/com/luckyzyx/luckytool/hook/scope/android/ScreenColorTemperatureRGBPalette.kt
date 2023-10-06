package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersionCode

object ScreenColorTemperatureRGBPalette : YukiBaseHooker() {
    override fun onHook() {
        val isEnable =
            prefs(ModulePrefs).getBoolean("enable_screen_color_temperature_rgb_palette", false)
        if (getOSVersionCode < 27 || !isEnable) return

        //Source OplusRgbBallManager -> oplus.software.display.rgb_ball_support
        "com.android.server.display.oplus.eyeprotect.manager.OplusRgbBallManager".toClass().apply {
            constructor { emptyParam() }.hook {
                after {
                    field { name = "mIsSupportColorModeRGB" }.get(instance).setTrue()
                    instance.current().method { name = "initRGBValueAnimator" }.call()
                }
            }
        }
    }
}