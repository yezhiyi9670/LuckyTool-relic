package com.luckyzyx.luckytool.hook.scope.screenshot

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.checkDataList

object CustomizeLongScreenshotMaxCapturedPages : YukiBaseHooker() {
    override fun onHook() {
        //Source ScrollCaptureConfigs -> scroll_configs_max_captured_pages / scroll_configs_max_captured_pixels
        //Source StitchLimitUtils -> isCapturedPagesReachLimit / trimToStitchLimit
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fieldCount(0)
                    methods {
                        add { returnType(IntType.name) }
                        add { returnType(BooleanType.name) }
                        add {
                            paramTypes(IntType.name, IntType.name)
                            returnType(IntType.name)
                        }
                    }
                    usingStrings("StitchLimitUtils")
                }
            }.apply {
                checkDataList("CustomizeLongScreenshotMaxCapturedPages")
                first().name.toClass().apply {
                    method {
                        param { it[1] == IntType }
                        paramCount = 2
                        returnType = BooleanType
                    }.hook { replaceToFalse() }
                    method {
                        param { it[1] == IntType && it[2] == IntType }
                        paramCount = 3
                        returnType = IntType
                    }.hook { replaceTo(-1) }
                }
            }
        }
    }
}