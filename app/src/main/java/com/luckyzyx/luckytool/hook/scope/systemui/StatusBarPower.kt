package com.luckyzyx.luckytool.hook.scope.systemui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object StatusBarPower : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        val removePercent = prefs(ModulePrefs).getBoolean("remove_statusbar_battery_percent", false)
        val userTypeface = prefs(ModulePrefs).getBoolean("statusbar_power_user_typeface", false)
        val customFontSize = prefs(ModulePrefs).getInt("statusbar_power_font_size", 0)

        //Source StatBatteryMeterView
        VariousClass(
            "com.oplusos.systemui.statusbar.widget.StatBatteryMeterView",  //C13
            "com.oplus.systemui.statusbar.pipeline.battery.ui.view.StatBatteryMeterView"  //C14
        ).toClass().apply {
            if (SDK >= A14) {
                constructor { paramCount = 3 }.hook {
                    after {
                        if (userTypeface) field { name = "defaultFontText" }.get(instance)
                            .set(Typeface.DEFAULT_BOLD)
                    }
                }
                method { name = "onFinishInflate" }.hook {
                    after {
                        val view = instance<View>()
                        view.findViewById<TextView>(
                            view.resources.getIdentifier(
                                "battery_text", "id",
                                this@StatusBarPower.packageName
                            )
                        )?.setTextSize(
                            TypedValue.COMPLEX_UNIT_DIP,
                            if (customFontSize == 0) 12F else customFontSize.toFloat() * 2
                        )
                        view.findViewById<TextView>(
                            view.resources.getIdentifier(
                                "battery_percentage_view", "id",
                                this@StatusBarPower.packageName
                            )
                        )?.setTextSize(
                            TypedValue.COMPLEX_UNIT_DIP,
                            if (customFontSize == 0) 12F else customFontSize.toFloat() * 2
                        )
                    }
                }
                return@apply
            }
            method { name = "onConfigChanged" }.hook {
                after {
                    method { name = "updatePercentText" }.get(instance).call()
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