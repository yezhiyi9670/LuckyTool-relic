package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarPower : YukiBaseHooker() {
    override fun onHook() {
        val removePercent = prefs(ModulePrefs).getBoolean("remove_statusbar_battery_percent", false)
        val userTypeface = prefs(ModulePrefs).getBoolean("statusbar_power_user_typeface", false)
        val customFontSize = prefs(ModulePrefs).getInt("statusbar_power_font_size", 0)

        //Source StatBatteryMeterView
        val clazz = if (SDK >= A14) "com.oplus.keyguard.covermanager.OplusCoverBatteryMeterView"
        else "com.oplusos.systemui.statusbar.widget.StatBatteryMeterView"
        clazz.toClassOrNull()?.apply {
            if (SDK < A14) method { name = "onConfigChanged" }.hook {
                after {
                    instance.current().method { name = "updatePercentText" }.call()
                }
            }
            method { name = "updatePercentText" }.hook {
                after {
                    field { name = "batteryPercentText" }.get(instance).cast<TextView>()?.apply {
                        if (removePercent) text = text.toString().replace("%", "")
                        if (userTypeface) {
                            typeface = Typeface.DEFAULT_BOLD
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_DIP,
                                if (customFontSize == 0) 12F else customFontSize.toFloat() * 2
                            )
                        }
                    }
                }
            }
        }
    }
}