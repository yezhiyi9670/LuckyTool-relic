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
import com.luckyzyx.luckytool.utils.DexkitPrefs
import com.luckyzyx.luckytool.utils.ModulePrefs
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

object ReplaceOnePlusModelWaterMark : YukiBaseHooker() {
    const val key = "replace_oneplus_model_watermark"

    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean(key, false)
        if (!isEnable) return

        val clsName = prefs(DexkitPrefs).getString(key, "null")
        //Source ConfigAbilityImpl
        findClass(clsName).hook {
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

    fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexKitBridge.create(appPath)?.use { bridge ->
            result = bridge.findClass {
                searchPackages = listOf("co", "ho", "jr", "qn", "ao", "nq", "hr", "es")
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
        }
        return result
    }
}