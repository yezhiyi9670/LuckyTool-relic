package com.luckyzyx.luckytool.hook.utils

import android.content.Context
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.scope.android.HookSystemProperties.toClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class WeatherInfoParseHelper(val classLoader: ClassLoader?) {

    val clazz = "com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper".toClass(classLoader)

    fun getInstance(): Any? {
        return clazz.method {
            name = "getInstance"
            emptyParam()
        }.get().call()
    }

    fun getLocalTimeInfo(context:Context): Any? {
        return clazz.method {
            name = "getLocalTimeInfo"
            paramCount = 1
        }.get(getInstance()).call(context)
    }

    fun getResidentTimeInfo(context:Context): Any? {
        return clazz.method {
            name = "getResidentTimeInfo"
            paramCount = 1
        }.get(getInstance()).call(context)
    }
}