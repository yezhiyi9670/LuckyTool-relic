package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass

@Suppress("unused", "MemberVisibilityCanBePrivate", "PrivatePropertyName")
class OplusBuildUtlils(val classLoader: ClassLoader? = null) {

    val clazz = "com.oplus.os.OplusBuild".toClass(classLoader)

    /**
     * 23 -> c12
     * 24 -> c12.1
     * 25 -> c12.2
     * 26 -> c13
     * 27 -> c13.1
     * 28 -> c13.1.1
     * 29 -> c13.2
     * 30 -> c14
     */
    private val VERSIONS = arrayOf(
        "V1.0", "V1.2", "V1.4", "V2.0", "V2.1", "V3.0", "V3.1", "V3.2", "V5.0", "V5.1",
        "V5.2", "V6.0", "V6.1", "V6.2", "V6.7", "V7", "V7.1", "V7.2", "V11", "V11.1",
        "V11.2", "V11.3", "V12", "V12.1", "V12.2", "V13", "V13.1", "V13.1.1", "V13.2", "V14.0",
        null
    )

    private val getOSVersions get() = clazz.field { name = "VERSIONS" }.get().cast<Array<String>>()

    val getOSVersionCode get() = clazz.method { name = "getOplusOSVERSION" }.get().invoke<Int>()

    val getOSVersionName get() = getOSVersionCode?.let { getOSVersions?.get(it - 1) }

}