package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.safeOfNull
import java.io.BufferedReader
import java.io.FileReader
import kotlin.math.roundToInt

object FixBatteryHealthDataDisplay : YukiBaseHooker() {
    val isEnable = prefs(ModulePrefs).getBoolean("fix_battery_health_data_display", false)
    override fun onHook() {
        if (SDK < A13) return
        //Source BatteryServiceExtImpl -> GuardElfThermalControl
        findClass("com.android.server.BatteryServiceExtImpl\$GuardElfThermalControl").hook {
            injectMember {
                method { name = "getUIsohValue" }
                afterHook {
                    if (!isEnable) return@afterHook
                    val curValue = safeOfNull {
                        BufferedReader(FileReader("/sys/class/oplus_chg/battery/battery_fcc")).readLine()
                            .trim { it <= ' ' }.toFloatOrNull()
                    }
                    val setValue = safeOfNull {
                        BufferedReader(FileReader("/sys/class/oplus_chg/battery/design_capacity")).readLine()
                            .trim { it <= ' ' }.toFloatOrNull()
                    }
                    result = if (curValue == null || setValue == null) -1
                    else (curValue / setValue * 100.0F).roundToInt()
                }
            }
        }
    }
}