package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.statusbar.StatusBarNotify.toClass
import com.luckyzyx.luckytool.utils.safeOf

@Suppress("unused")
class OplusBuildUtlils(val classLoader: ClassLoader?) {

    val clazz = "com.oplus.os.OplusBuild".toClass(classLoader)

    val getVersions get() = clazz.field { name = "VERSIONS" }.get().array<String>()

    fun getOplusOSVERSION(): Int? {
        return clazz.method {
            name = "getOplusOSVERSION"
            emptyParam()
        }.get().invoke<Int>()
    }

    fun getCurrentOSVersion(): Double = safeOf(0.0) {
        val version = getOplusOSVERSION()?.let { getVersions[it - 1] }
        if (!version.isNullOrBlank()) return@safeOf version.replace("V", "").toDouble()
        return 0.0
    }
}