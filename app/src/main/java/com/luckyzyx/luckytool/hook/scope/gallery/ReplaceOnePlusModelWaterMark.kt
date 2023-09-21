package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils

object ReplaceOnePlusModelWaterMark : YukiBaseHooker() {

    override fun onHook() {
        //Source ConfigAbilityImpl
        DexkitUtils.searchDexClass(
            "ReplaceOnePlusModelWaterMark", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
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
            }
        }?.firstOrNull()?.className?.hook {
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
}