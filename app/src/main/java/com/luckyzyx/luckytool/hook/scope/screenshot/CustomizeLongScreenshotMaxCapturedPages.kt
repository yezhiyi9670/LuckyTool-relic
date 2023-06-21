package com.luckyzyx.luckytool.hook.scope.screenshot

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType

object CustomizeLongScreenshotMaxCapturedPages : YukiBaseHooker() {
    override fun onHook() {
        //Source StitchLimitUtils -> scroll_configs_max_captured_pages / scroll_configs_max_captured_pixels
        searchClass {
            from("ac", "yb", "zb", "hb", "ib").absolute()
            field().none()
            method { returnType = IntType }.count(6)
            method { returnType = BooleanType }.count(2)
            method { param { it[1] == IntType } }.count(6)
            method { param(IntType, IntType);returnType = IntType }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    param { it[1] == IntType }
                    paramCount = 2
                    returnType = BooleanType
                }
                replaceToFalse()
            }
            injectMember {
                method {
                    param { it[1] == IntType && it[2] == IntType }
                    paramCount = 3
                    returnType = IntType
                }
                replaceTo(-1)
            }
        } ?: loggerD(msg = "$packageName\nError -> CustomizeLongScreenshotMaxCapturedPages")
    }
}