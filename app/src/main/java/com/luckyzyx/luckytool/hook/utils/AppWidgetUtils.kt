package com.luckyzyx.luckytool.hook.utils

import android.appwidget.AppWidgetManager
import android.content.Context
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass

@Suppress("unused")
class AppWidgetUtils(classLoader: ClassLoader?) {

    val clazz = "android.appwidget.AppWidgetManager".toClass(classLoader)
    fun getInstance(context: Context): AppWidgetManager? {
        return clazz.method {
            name = "getInstance"
            param(ContextClass)
        }.get().invoke<AppWidgetManager>(context)
    }
}