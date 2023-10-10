package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method

object HookThermalController : YukiBaseHooker() {
    override fun onHook() {
        //Source ThermalControlConfig
//        "com.oplus.thermalcontrol.ThermalControlConfig".toClass().apply {
//            method { name = "isThermalControlEnable" }.hook {
//                replaceToFalse()
//            }
//        }
        //Source ThermalControllerCenter
        "com.oplus.thermalcontrol.ThermalControllerCenter".toClass().apply {
            method { name = "start5GModemMethods" }.hook {
                intercept()
            }
            method { name = "startCollingMethods" }.hook {
                intercept()
            }
            method { name = "startRestrictMethods" }.hook {
                intercept()
            }
            method { name = "startCategoryChangeMethods" }.hook {
                intercept()
            }
            method { name = "stopCollingMethods" }.hook {
                before {
                    field { name = "mThermalControlState" }.get(instance).setTrue()
                }
            }
        }
        //Source ThermalControlUtils
        "com.oplus.thermalcontrol.ThermalControlUtils".toClass().apply {
            method { name = "getCurrentThermalStatus" }.hook {
                replaceTo(-1)
            }
            method { name = "getCurrentTemperature" }.hook {
                after {
                    val temp = result<Float>() ?: return@after
                    if (temp > 30.0F) result = 30.0F
                }
            }
            method { name = "getTempFromBind" }.hook {
                after {
                    val temp = result<Float>() ?: return@after
                    if (temp > 30.0F) result = 30.0F
                }
            }
            method { name = "sendThermalLevelChangeBroadcast" }.hook {
                before {
                    args().first().set(-1)
                    args().last().set(30)
                }
            }
            method {
                name = "setChargingLevel"
                paramCount(1..3)
            }.hook {
                before {
                    args(0).set(0)
                    if (args.size >= 3) {
                        args(1).set(0)
                        field { name = "mIsSpeedCharging" }.get(instance).setTrue()
                    }
                }
            }
            method {
                name = "setFps"
                paramCount = 2
            }.hook {
                before { args(0).set(0) }
            }
        }
    }
}