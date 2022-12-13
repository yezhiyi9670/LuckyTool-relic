package com.luckyzyx.luckytool.hook.apps.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringType
import java.util.concurrent.ConcurrentHashMap

class EnableEVATheme : YukiBaseHooker() {
    override fun onHook() {
        //Source SystemPropertiesHelper
        //Search isEvaThemePhone
        searchClass {
            from("ic", "hc", "jc").absolute()
            field {
                type = ConcurrentHashMap::class.java
            }.count(3)
            method {
                emptyParam()
                returnType = BooleanType
            }.count(5..7)
            method {
                emptyParam()
                returnType = IntType
            }.count(2)
            method {
                emptyParam()
                returnType = LongType
            }.count(1)
            method {
                param(StringType)
                paramCount = 1
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}