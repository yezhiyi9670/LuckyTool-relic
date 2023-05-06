package com.luckyzyx.luckytool.hook.utils

import android.os.IBinder
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimit.toClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ServiceManagerUtils(classLoader: ClassLoader?) {

    val clazz = "android.os.ServiceManager".toClass(classLoader)

    fun getService(serviceName: String): IBinder? {
        return clazz.method {
            name = "getService"
            param(StringClass)
        }.get().invoke<IBinder>(serviceName)
    }
}