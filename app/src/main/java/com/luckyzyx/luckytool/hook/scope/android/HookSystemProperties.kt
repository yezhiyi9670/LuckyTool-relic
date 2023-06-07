package com.luckyzyx.luckytool.hook.scope.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.StringClass

object HookSystemProperties : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemProperties
        findClass("android.os.SystemProperties").hook {
            injectMember {
                method {
                    name = "get"
                    param(StringClass, StringClass)
                    returnType = StringClass
                }
                afterHook {
                    when (args().first().string()) {
                        "persist.oplus.display.vrr.adfr" -> {
                            loggerD(msg = "adfr -> " + result.toString())
                            result = "0"
                        }

                        "persist.oplus.display.vrr" -> {
                            loggerD(msg = "vrr -> " + result.toString())
                            result = "0"
                        }
                    }
                }
            }
        }
    }
}