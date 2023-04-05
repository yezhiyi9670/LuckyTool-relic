package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object BatteryOptimizationWhitelist : YukiBaseHooker() {
    override fun onHook() {
        val isEnable =
            prefs(ModulePrefs).getBoolean("restore_default_battery_optimization_whitelist", false)
        if (!isEnable) return
        //Source OplusDeviceIdleHelper
        //Search sys_deviceidle_whitelist
        findClass("com.android.server.OplusDeviceIdleHelper").hook {
            val getNewWhiteList = instanceClass.hasMethod { name = "getNewWhiteList" }
            val getNewWhiteListLocked =
                instanceClass.hasMethod { name = "getNewWhiteListLocked" }
            if ((getNewWhiteList && getNewWhiteListLocked) || !(getNewWhiteList || getNewWhiteListLocked)) return@hook
            injectMember {
                method {
                    if (getNewWhiteList) name = "getNewWhiteList"
                    if (getNewWhiteListLocked) name = "getNewWhiteListLocked"
                    paramCount = 1
                }
                replaceUnit {
                    val whiteListAll = args().first().cast<java.util.ArrayList<String>>()
                    whiteListAll?.clear()
                    val mDefaultWhitelist = field {
                        name = "mDefaultWhitelist"
                    }.get().list<String>()
                    whiteListAll?.addAll(mDefaultWhitelist)
                    method {
                        name = "getCustomizeWhiteList"
                    }.get(instance).call(whiteListAll)
                    method {
                        name = "addNfcJapanFelica"
                    }.get(instance).call(whiteListAll)
//                    whiteListAll?.add("com.oplus.upgradeguide")
                }
            }
        }
    }
}