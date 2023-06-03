package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object HookThermalController : YukiBaseHooker() {
    override fun onHook() {
        //Source ThermalControllerCenter
        findClass("com.oplus.thermalcontrol.ThermalControllerCenter").hook {
            injectMember {
                method { name = "startCollingMethods" }
                intercept()
            }
            injectMember {
                method { name = "startRestrictMethods" }
                intercept()
            }
            injectMember {
                method { name = "startCategoryChangeMethods" }
                intercept()
            }
        }
        //Source ThermalControlUtils
        findClass("com.oplus.thermalcontrol.ThermalControlUtils").hook {
            injectMember {
                method { name = "getCurrentThermalStatus" }
                replaceTo(0)
            }
            injectMember {
                method { name = "getCurrentTemperature" }
                afterHook {
                    val temp = result<Float>() ?: return@afterHook
                    if (temp > 30.0F) result = 30.0F
                }
            }
            injectMember {
                method { name = "getTempFromBind" }
                afterHook {
                    val temp = result<Float>() ?: return@afterHook
                    if (temp > 30.0F) result = 30.0F
                }
            }
        }
    }
}