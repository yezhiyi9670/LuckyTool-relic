package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.statusbar.StatusBarNotify.toClass
import com.luckyzyx.luckytool.utils.safeOf

@Suppress("unused", "MemberVisibilityCanBePrivate")
class OplusBuildUtlils(val classLoader: ClassLoader?) {

    val clazz = "com.oplus.os.OplusBuild".toClass(classLoader)

    /**
     * 获取OS版本数组
     */
    val getVersions get() = clazz.field { name = "VERSIONS" }.get().array<String>()

    fun getOplusOSVERSION(): Int? {
        return clazz.method {
            name = "getOplusOSVERSION"
            emptyParam()
        }.get().invoke<Int>()
    }

    /**
     * 获取当前OS版本
     * @return Double
     */
    fun getCurrentOSVersion(): Double = safeOf(0.0) {
        val version = getOplusOSVERSION()?.let { getVersions[it - 1] }
        if (!version.isNullOrBlank()) return@safeOf version.replace("V", "").toDouble()
        return 0.0
    }
}