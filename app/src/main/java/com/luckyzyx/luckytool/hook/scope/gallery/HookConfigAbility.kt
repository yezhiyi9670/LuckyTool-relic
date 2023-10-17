package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.checkDataList
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookConfigAbility : YukiBaseHooker() {

    override fun onHook() {
        //启用水印编辑
        val waterMark = prefs(ModulePrefs).getBoolean("enable_watermark_editing", false)
        //替换OnePlus机型水印
        val notOplus = prefs(ModulePrefs).getBoolean("replace_oneplus_model_watermark", false)

        //Source ConfigAbilityImpl
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
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
            }.apply {
                checkDataList("HookConfigAbility")
                val member = first()
                member.name.toClass().apply {
                    method {
                        param(StringClass, BooleanType)
                        returnType = BooleanClass
                    }.hook {
                        after {
                            when (args().first().string()) {
                                "is_oneplus_brand" -> if (notOplus) resultFalse()
                                "feature_is_support_watermark" -> if (waterMark) resultTrue()
                                "feature_is_support_hassel_watermark" -> if (waterMark) resultTrue()
                                "feature_is_support_photo_editor_watermark" -> if (waterMark) resultTrue()
                                "feature_is_support_privacy_watermark" -> if (waterMark) resultTrue()
                                "feature_is_support_lns" -> {
                                    //                            loggerD(msg = "ConfigAbility -> lns call -> $result")
                                    //                            resultTrue()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}