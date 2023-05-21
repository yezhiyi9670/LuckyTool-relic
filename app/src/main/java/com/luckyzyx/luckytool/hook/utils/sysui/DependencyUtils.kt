package com.luckyzyx.luckytool.hook.utils.sysui

import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimit.toClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class DependencyUtils(classLoader: ClassLoader?) {

    val clazz = "com.android.systemui.Dependency".toClass(classLoader)

    fun get(cls: Class<*>): Any? {
        return clazz.method {
            name = "get"
            param(Class::class.java)
        }.get().invoke<Any>(cls)
    }

}