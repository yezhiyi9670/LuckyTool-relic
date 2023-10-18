package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveBatteryTemperatureControl : YukiBaseHooker() {
    override fun onHook() {
        //Source ThermalControllerCenter
        "com.oplus.thermalcontrol.ThermalControllerCenter".toClass().apply {
            method { name = "onStart" }.hook {
                after { method { name = "onDestory" }.get(instance).call() }
            }
        }
    }
}