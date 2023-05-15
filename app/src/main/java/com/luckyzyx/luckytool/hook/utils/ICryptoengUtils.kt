package com.luckyzyx.luckytool.hook.utils

import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.scope.systemui.FingerPrintIconAnim.toClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ICryptoengUtils(val classLoader: ClassLoader?) {

    val clazz = "vendor.oplus.hardware.cryptoeng.V1_0.ICryptoeng".toClass(classLoader)

    fun getService(): Any? {
        return clazz.method {
            name = "getService"
            emptyParam()
        }.get().call()
    }

    fun cryptoengInvokeCommand(arrayList: ArrayList<Byte>): ArrayList<Byte>? {
        return clazz.method {
            name = "cryptoeng_invoke_command"
            paramCount = 1
        }.get(getService()).invoke<ArrayList<Byte>>(arrayList)
    }
}