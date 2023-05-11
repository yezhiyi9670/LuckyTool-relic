package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.scope.screenshot.RemoveScreenshotPrivacyLimit.toClass

@Suppress("unused")
class OplusFeatureConfigManagerUtils(val classLoader: ClassLoader?) {

    val clazz = "com.oplus.content.OplusFeatureConfigManager".toClass(classLoader)

    fun getInstance(): Any? {
        return clazz.method {
            name = "getInstance"
            emptyParam()
        }.get().call()
    }

}