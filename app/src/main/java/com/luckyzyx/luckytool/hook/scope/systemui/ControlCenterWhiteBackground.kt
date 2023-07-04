package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.safeOfNull

object ControlCenterWhiteBackground : YukiBaseHooker() {
    override fun onHook() {
        var customAlpha =
            prefs(ModulePrefs).getInt("custom_control_center_background_transparency", -1)
        dataChannel.wait<Int>("custom_control_center_background_transparency") {
            customAlpha = it
        }
        //Source ScrimController
        findClass("com.android.systemui.statusbar.phone.ScrimController").hook {
            injectMember {
                method { name = "updateScrimColor" }
                beforeHook {
                    if (customAlpha < 0) return@beforeHook
                    val value = customAlpha / 10.0F
                    val view = args().first().cast<View>() ?: return@beforeHook
                    val alpha = args(1).cast<Float>() ?: return@beforeHook
                    val name = safeOfNull { view.resources.getResourceEntryName(view.id) }
                        ?: return@beforeHook
                    when (name) {
                        "scrim_in_front" -> {}
                        "scrim_behind" -> if (alpha > value) args(1).set(value)
                        "scrim_notifications" -> if (alpha > value) args(1).set(value)
                    }
                }
            }
        }
    }
}