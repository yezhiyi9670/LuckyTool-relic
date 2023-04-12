package com.luckyzyx.luckytool.hook.scope.settings

import android.content.Context
import android.provider.Settings
import android.util.DisplayMetrics
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import kotlin.math.max
import kotlin.math.min

object RemoveDpiRestartRecovery : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusDensityPreference
        findClass("com.oplus.settings.widget.preference.OplusDensityPreference").hook {
            injectMember {
                method {
                    name = "onPreferenceChange"
                    paramCount = 2
                }
                afterHook {
                    val newValue = args().last().string()
                    val context = instance.current().method {
                        name = "getContext"
                        superClass()
                    }.invoke<Context>() ?: return@afterHook
                    val displayMetrics: DisplayMetrics =
                        context.applicationContext.resources.displayMetrics
                    val min: Int = min(
                        displayMetrics.widthPixels,
                        displayMetrics.heightPixels
                    ) * 160 / max(newValue.toInt(), 320)
                    val max = max(min, 120)
                    Settings.Secure.putString(
                        context.contentResolver,
                        "display_density_forced",
                        max.toString()
                    )
                    instance.current().method {
                        name = "notifyChanged"
                        superClass()
                    }.call()
                }
            }
        }
    }
}