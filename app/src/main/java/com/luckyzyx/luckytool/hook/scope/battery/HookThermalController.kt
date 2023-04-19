package com.luckyzyx.luckytool.hook.scope.battery

import androidx.collection.ArrayMap
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.log.loggerD

object HookThermalController : YukiBaseHooker() {
    override fun onHook() {
        val isDebug = false
        //Source ThermalControllerCenter
        findClass("com.oplus.thermalcontrol.ThermalControllerCenter").hook {
            injectMember {
                method {
                    name = "startCollingMethods"
                    paramCount = 3
                }
                beforeHook {
                    val list = ArrayMap<String, Any>()
                    args().first().any()?.current {
                        list["categoryName"] = field { name = "categoryName" }.string()
//                        list["configItemName"] = field { name = "configItemName" }.string()
//                        list["gearLevel"] = field { name = "gearLevel" }.int()

//                        list["brightness"] = field { name = "brightness" }.int()
                        list["charge"] = field { name = "charge" }.int()
                        field { name = "charge" }.set(3)
//                        list["speedChargeAdd"] = field { name = "speedChargeAdd" }.int()
//                        field { name = "speedChargeAdd" }.set(1)

                        list["modem"] = field { name = "modem" }.int()
                        field { name = "modem" }.set(0)

                        list["fps"] = field { name = "fps" }.int()
                        field { name = "fps" }.set(0)
                        list["refreshRate"] = field { name = "refreshRate" }.int()
                        field { name = "refreshRate" }.set(0)
//                        list["resolution"] = field { name = "resolution" }.int()

                        list["cpu"] = field { name = "cpu" }.int()
                        list["gpu"] = field { name = "gpu" }.int()
                        list["restrict"] = field { name = "restrict" }.int()
                        field { name = "restrict" }.set(0)
                        list["thermalSerious"] = field { name = "thermalSerious" }.int()
                        field { name = "thermalSerious" }.set(0)

//                        list["cameraBrightness"] = field { name = "cameraBrightness" }.int()

//                        list["disFlashlight"] = field { name = "disFlashlight" }.int()
                        field { name = "disFlashlight" }.set(0)
//                        list["stopCameraVideo"] = field { name = "stopCameraVideo" }.int()
                        field { name = "stopCameraVideo" }.set(0)
//                        list["disCamera"] = field { name = "disCamera" }.int()
                        field { name = "disCamera" }.set(0)
//                        list["disWifiHotSpot"] = field { name = "disWifiHotSpot" }.int()
                        field { name = "disWifiHotSpot" }.set(0)
//                        list["disTorch"] = field { name = "disTorch" }.int()
                        field { name = "disTorch" }.set(0)
//                        list["disFrameInsert"] = field { name = "disFrameInsert" }.int()
                        field { name = "disFrameInsert" }.set(0)

//                        list["disVideoSR"] = field { name = "disVideoSR" }.int()
                        field { name = "disVideoSR" }.set(0)
//                        list["disOISE"] = field { name = "disOISE" }.int()
                        field { name = "disOISE" }.set(0)
//                        list["disHBMHB"] = field { name = "disHBMHB" }.int()
                        field { name = "disHBMHB" }.set(0)
                    }
                    if (isDebug) loggerD(msg = "startCollingMethods\n$list")
                }
            }
            injectMember {
                method {
                    name = "startCategoryChangeMethods"
                    paramCount = 1
                }
                beforeHook {
                    val list = ArrayMap<String, Any>()
                    args().first().any()?.current {
                        list["categoryName"] = field { name = "categoryName" }.string()

                        list["charge"] = field { name = "charge" }.int()
                        field { name = "charge" }.set(3)
//                        list["speedChargeAdd"] = field { name = "speedChargeAdd" }.int()
//                        field { name = "speedChargeAdd" }.set(1)

                        list["fps"] = field { name = "fps" }.int()
                        field { name = "fps" }.set(0)
                        list["refreshRate"] = field { name = "refreshRate" }.int()
                        field { name = "refreshRate" }.set(0)
                    }
                    if (isDebug) loggerD(msg = "startCategoryChangeMethods\n$list")
                }
            }
        }
    }
}