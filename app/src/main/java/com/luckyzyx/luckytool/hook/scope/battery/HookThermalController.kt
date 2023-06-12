package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object HookThermalController : YukiBaseHooker() {
    override fun onHook() {
        //Source ThermalControllerCenter
        findClass("com.oplus.thermalcontrol.ThermalControllerCenter").hook {
            injectMember {
                method { name = "start5GModemMethods" }
                intercept()
            }
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
            injectMember {
                method { name = "stopCollingMethods" }
                beforeHook {
                    field { name = "mThermalControlState" }.get(instance).setTrue()
                }
            }
        }
        //Source ThermalControlUtils
        findClass("com.oplus.thermalcontrol.ThermalControlUtils").hook {
            injectMember {
                method { name = "getCurrentThermalStatus" }
                replaceTo(-1)
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
            injectMember {
                method {
                    name = "setChargingLevel"
                    paramCount(1..3)
                }
                beforeHook {
                    args(0).set(0)
                    if (args.size >= 3) {
                        args(1).set(0)
                        field { name = "mIsSpeedCharging" }.get(instance).setTrue()
                    }
                }
            }
            injectMember {
                method {
                    name = "setFps"
                    paramCount = 2
                }
                beforeHook { args(0).set(0) }
            }
        }
    }
}