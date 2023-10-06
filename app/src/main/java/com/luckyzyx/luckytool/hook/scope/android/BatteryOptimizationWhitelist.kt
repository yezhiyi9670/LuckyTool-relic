package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.ModulePrefs

object BatteryOptimizationWhitelist : YukiBaseHooker() {
    override fun onHook() {
        val isEnable =
            prefs(ModulePrefs).getBoolean("restore_default_battery_optimization_whitelist", false)
        val disableCustom = false
//            prefs(ModulePrefs).getBoolean("disable_customize_battery_optimization_whiteList", false)
        if (!isEnable) return

        //Source oplus-service-jobscheduler -> OplusDeviceIdleHelper
        //Search sys_deviceidle_whitelist
        "com.android.server.OplusDeviceIdleHelper".toClass().apply {
            method {
                name = if (hasMethod { name = "getNewWhiteList" }) "getNewWhiteList"
                else if (hasMethod { name = "getNewWhiteListLocked" }) "getNewWhiteListLocked"
                else return
                paramCount = 1
            }.hook {
                replaceUnit {
                    val whiteListAll = args().first().cast<java.util.ArrayList<String>>()
                    whiteListAll?.clear()
                    val mDefaultWhitelist =
                        field { name = "mDefaultWhitelist" }.get().list<String>()
                    whiteListAll?.addAll(mDefaultWhitelist)

                    if (!disableCustom) instance.current().method { name = "getCustomizeWhiteList" }
                        .call(whiteListAll)
                    instance.current().method { name = "addNfcJapanFelica" }.call(whiteListAll)
//                    whiteListAll?.add("com.oplus.upgradeguide")
                }
            }
        }
    }
}