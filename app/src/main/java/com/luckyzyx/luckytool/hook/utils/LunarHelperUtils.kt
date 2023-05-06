package com.luckyzyx.luckytool.hook.utils

import android.content.Context
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.luckytool.hook.statusbar.StatusBarClock.toClass

class LunarHelperUtils(val classLoader: ClassLoader?) {

    val clazz = "com.oplusos.systemui.keyguard.clock.LunarHelper".toClass(classLoader)

    /**
     * 构建日历对象实例
     * @param context Context
     * @return Any?
     */
    fun buildInstance(context: Context): Any? {
        return clazz.buildOf(context) {
            param(ContextClass)
        }
    }

    /**
     * 获取日历字符串
     * @param instance Any
     * @param time Long
     * @return String?
     */
    fun getDateToString(instance: Any?, time: Long): String? {
        return clazz.method {
            name = "getDateToString"
            param(LongType)
        }.get(instance).invoke<String>(time)
    }
}