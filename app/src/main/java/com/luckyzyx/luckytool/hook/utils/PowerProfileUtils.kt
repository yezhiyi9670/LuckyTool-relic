package com.luckyzyx.luckytool.hook.utils

import android.content.Context
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.luckyzyx.luckytool.hook.scope.systemui.RemoveDoNotDisturbModeNotification.toClass

@Suppress("unused")
class PowerProfileUtils(val classLoader: ClassLoader?) {

    val clazz = "com.android.internal.os.PowerProfile".toClass()

    fun buildInstance(context: Context?): Any? {
        return clazz.buildOf(context) {
            param(ContextClass)
        }
    }

    fun getBatteryCapacity(instance: Any?): Double? {
        return clazz.method {
            name = "getBatteryCapacity"
        }.get(instance).invoke<Double>()
    }

}