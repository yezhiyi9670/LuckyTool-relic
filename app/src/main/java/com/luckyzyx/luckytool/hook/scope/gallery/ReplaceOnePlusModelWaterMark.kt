package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import org.luckypray.dexkit.DexKitBridge

object ReplaceOnePlusModelWaterMark : YukiBaseHooker() {
    override fun onHook() {
        val apkPath = appInfo.sourceDir
        val bridge = DexKitBridge.create(apkPath) ?: run {
            loggerD(msg = "$packageName\nError -> ReplaceOnePlusModelWaterMark")
            return
        }
        bridge.findClass {
            searchPackages = listOf("co", "ho", "jr", "qn", "ao", "nq", "hr", "es")
            matcher {
                fields {
                    addForType(ContextClass.name)
                    count = 3
                }
                methods {
                    add { name = "close";paramCount = 0 }
                    add { name = "contains";paramTypes = listOf(StringClass.name) }
                    add { returnType = AutoCloseable::class.java.name }
                    add {
                        paramTypes = listOf(StringClass.name, IntType.name)
                        returnType = IntClass.name
                    }
                    add {
                        paramTypes = listOf(StringClass.name, LongType.name)
                        returnType = LongClass.name
                    }
                    add {
                        paramTypes = listOf(StringClass.name, StringClass.name)
                        returnType = StringClass.name
                    }
                    add {
                        paramTypes = listOf(StringClass.name, BooleanType.name)
                        returnType = BooleanClass.name
                    }
                }
            }
        }.forEach {
            findClass(it.className).hook {
                injectMember {
                    method {
                        param(StringClass, BooleanType)
                        returnType = BooleanClass
                    }
                    beforeHook {
                        when (args().first().string()) {
                            "is_oneplus_brand" -> resultFalse()
                        }
                    }
                }
            }
        }
        bridge.close()
    }
}