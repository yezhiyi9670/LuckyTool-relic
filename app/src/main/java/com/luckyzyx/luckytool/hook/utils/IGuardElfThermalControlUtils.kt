package com.luckyzyx.luckytool.hook.utils

import android.os.IBinder
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.IBinderClass
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimit.toClass

@Suppress("unused")
class IGuardElfThermalControlUtils(classLoader: ClassLoader) {
    val clazz = "android.os.IGuardElfThermalControl".toClass(classLoader)

    fun getInstance(iBinder: IBinder): Any? {
        return "android.os.IGuardElfThermalControl\$Stub".toClass().method {
            name = "asInterface"
            param(IBinderClass)
        }.get().call(iBinder)
    }
}