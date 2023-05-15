package com.luckyzyx.luckytool.hook.utils

import android.content.Context
import android.os.IBinder
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.highcapable.yukihookapi.hook.type.android.IBinderClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class IDisplayManagerUtils(val classLoader: ClassLoader?) {

    val clazz = "android.hardware.display.IDisplayManager".toClass(classLoader)
    val stub = "android.hardware.display.IDisplayManager\$Stub".toClass(classLoader)

    fun getService(): IBinder? {
        return ServiceManagerUtils(null).getService(Context.DISPLAY_SERVICE)
    }

    fun getInstance(iBinder: IBinder?): Any? {
        return stub.method {
            name = "asInterface"
            param(IBinderClass)
        }.get().call(iBinder)
    }

    fun setSpecBrightness(instance: Any?, gear: Int, reason: String, rate: Int) {
        clazz.method {
            name = "setSpecBrightness"
            param(IntType, StringClass, IntType)
        }.get(instance).call(gear, reason, rate)
    }
}