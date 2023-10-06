package com.luckyzyx.luckytool.hook.scope.battery

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.TextViewClass
import com.highcapable.yukihookapi.hook.type.android.ViewClass
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.filterNumber

object BatteryHealthDataTips : YukiBaseHooker() {
    @SuppressLint("DiscouragedApi", "SetTextI18n")
    override fun onHook() {
        //Source BatteryHealthDataPreference
        "com.oplus.powermanager.fuelgaue.BatteryHealthDataPreference".toClass().apply {
            method { param(ViewClass) }.hook {
                after {
                    val view = args().first().cast<View>() ?: return@after
                    view.context.injectModuleAppResources()
                    val content = view.findViewById<TextView>(
                        view.resources.getIdentifier(
                            "max_capacity_content",
                            "id", this@BatteryHealthDataTips.packageName
                        )
                    ) ?: return@after
                    val data = field { type = TextViewClass }.get(instance).cast<TextView>()
                        ?: return@after
                    val num = data.text.filterNumber.toIntOrNull()
                    val tipStr =
                        view.context.getString(R.string.fix_battery_health_data_display_tips)
                    val tips = if (num == null) "${tipStr}\n" else ""
                    content.apply {
                        gravity = Gravity.CENTER
                        text = "$text\n${tips}By: LuckyTool"
                    }
                }
            }
        }
    }
}