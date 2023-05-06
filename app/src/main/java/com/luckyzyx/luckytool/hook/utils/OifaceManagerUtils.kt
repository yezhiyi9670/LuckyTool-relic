package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarBatteryInfoNotify.toClass

@Suppress("MemberVisibilityCanBePrivate", "unused")
class OifaceManagerUtils(classLoader: ClassLoader?) {

    val clazz = "com.oplus.oiface.OifaceManager".toClass(classLoader)

    fun getInstance(): Any? {
        return clazz.method {
            name = "getInstance"
            param(StringClass)
        }.get().call("")
    }

    fun getInstalledGameList(): Array<String>? {
        return clazz.method {
            name = "getInstalledGameList"
            emptyParam()
        }.get(getInstance()).invoke<Array<String>>()
    }

    fun getGameModeStatus(): Int? {
        return clazz.method {
            name = "getGameModeStatus"
            emptyParam()
        }.get(getInstance()).invoke<Int>()
    }

    fun setTouchResponsiveness(level: Int): Boolean? {
        return clazz.method {
            name = "setTouchResponsiveness"
            param(IntType)
        }.get(getInstance()).invoke<Boolean>(level)
    }

    fun setTouchSensibility(level: Int): Boolean? {
        return clazz.method {
            name = "setTouchSensibility"
            param(IntType)
        }.get(getInstance()).invoke<Boolean>(level)
    }
}