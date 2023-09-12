package com.luckyzyx.luckytool.hook.utils

import android.os.IBinder
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.highcapable.yukihookapi.hook.type.android.IBinderClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

@Suppress("unused", "MemberVisibilityCanBePrivate")
class IColorDisplayUtils(val classLoader: ClassLoader?) {

    val serviceName = "color_display"
    val iManager = "android.hardware.display.IColorDisplayManager".toClass(classLoader)
    val stub = "android.hardware.display.IColorDisplayManager\$Stub".toClass(classLoader)
    val clazz = "android.hardware.display.ColorDisplayManager".toClass(classLoader)
    val internal = "android.hardware.display.ColorDisplayManager\$ColorDisplayManagerInternal"
        .toClass(classLoader)

    fun getService(): IBinder? {
        return ServiceManagerUtils(null).getService(serviceName)
    }

    fun getInstance(iBinder: IBinder?): Any? {
        val iInterface = stub.method {
            name = "asInterface"
            param(IBinderClass)
        }.get().call(iBinder)
        return internal.buildOf(iInterface) { param(iManager) }
    }

    fun isReduceBrightColorsActivated(instance: Any?): Boolean? {
        return clazz.method {
            name = "isReduceBrightColorsActivated"
            emptyParam()
        }.get(instance).invoke<Boolean>()
    }

    fun setReduceBrightColorsActivated(instance: Any?, active: Boolean): Boolean? {
        return clazz.method {
            name = "setAllowedNetworkTypesForReason"
            param(BooleanType)
        }.get(instance).invoke<Boolean>(active)
    }
}