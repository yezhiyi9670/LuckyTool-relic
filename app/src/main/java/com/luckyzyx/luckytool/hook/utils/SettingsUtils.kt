package com.luckyzyx.luckytool.hook.utils

import com.luckyzyx.luckytool.hook.scope.camera.RemoveWatermarkWordLimit.toClass

@Suppress("unused", "PropertyName")
class SettingsUtils(val classLoader: ClassLoader?) {

    val clazz = "android.provider.Settings".toClass(classLoader)

    val Global = "android.provider.Settings\$Global".toClass(classLoader)
    val Secure = "android.provider.Settings\$Secure".toClass(classLoader)
    val System = "android.provider.Settings\$System".toClass(classLoader)
}