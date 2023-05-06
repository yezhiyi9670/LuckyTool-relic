package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarBatteryInfoNotify.toClass

@Suppress("MemberVisibilityCanBePrivate", "unused")
class IChargerUtils(classLoader: ClassLoader?) {

    val clazz = "vendor.oplus.hardware.charger.V1_0.ICharger".toClass(classLoader)

    fun getDefaultInstance(): Any? {
        return clazz.method {
            name = "getService"
            emptyParam()
        }.get().call()
    }

    fun queryChargeInfo(): String? {
        return clazz.method {
            name = "queryChargeInfo"
            emptyParam()
        }.get(getDefaultInstance()).invoke<String>()
    }

    fun getUIsohValue(): Int? {
        return clazz.method {
            name = "getUIsohValue"
            emptyParam()
        }.get(getDefaultInstance()).invoke<Int>()
    }
}