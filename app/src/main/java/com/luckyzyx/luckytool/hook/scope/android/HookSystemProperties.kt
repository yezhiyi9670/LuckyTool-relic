package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.java.StringClass

object HookSystemProperties : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemProperties
        "android.os.SystemProperties".toClass().apply {
            method {
                name = "get"
                param(StringClass, StringClass)
                returnType = StringClass
            }.hook {
                after {
                    when (args().first().string()) {
                        "persist.oplus.display.vrr.adfr" -> {
                            YLog.debug("adfr -> " + result.toString())
                            result = "0"
                        }

                        "persist.oplus.display.vrr" -> {
                            YLog.debug("vrr -> " + result.toString())
                            result = "0"
                        }
                    }
                }
            }
        }
    }
}