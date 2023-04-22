package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarBatteryInfoNotify.toClass

@Suppress("MemberVisibilityCanBePrivate")
class IChargerUtils(classLoader: ClassLoader) {

    private val clazz = "vendor.oplus.hardware.charger.V1_0.ICharger".toClass(classLoader)

    fun getService(): Any? {
        return clazz.method {
            name = "getService"
            emptyParam()
        }.get().call()
    }

    fun queryChargeInfo(): String? {
        return clazz.method {
            name = "queryChargeInfo"
            emptyParam()
        }.get(getService()).invoke<String>()
    }
}