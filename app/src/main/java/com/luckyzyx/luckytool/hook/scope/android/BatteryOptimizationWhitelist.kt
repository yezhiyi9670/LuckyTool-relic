package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
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
        findClass("com.android.server.OplusDeviceIdleHelper").hook {
            injectMember {
                method {
                    name = if (instanceClass.hasMethod { name = "getNewWhiteList" }) "getNewWhiteList"
                    else if (instanceClass.hasMethod { name = "getNewWhiteListLocked" }) "getNewWhiteListLocked"
                    else return
                    paramCount = 1
                }
                replaceUnit {
                    val whiteListAll = args().first().cast<java.util.ArrayList<String>>()
                    whiteListAll?.clear()
                    val mDefaultWhitelist = field { name = "mDefaultWhitelist" }.get().list<String>()
                    whiteListAll?.addAll(mDefaultWhitelist)

                    if (!disableCustom) method { name = "getCustomizeWhiteList" }.get(instance)
                        .call(whiteListAll)
                    method { name = "addNfcJapanFelica" }.get(instance).call(whiteListAll)
//                    whiteListAll?.add("com.oplus.upgradeguide")
                }
            }
        }
    }
}