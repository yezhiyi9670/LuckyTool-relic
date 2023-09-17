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

object ReplaceOnePlusModelWaterMark : YukiBaseHooker() {
    override fun onHook() {
        //Source ConfigAbilityImpl
        searchClass {
            from("co", "ho", "jr", "qn", "ao", "nq", "hr").absolute()
            field { type = ContextClass }.count(1)
            method { name = "close";emptyParam() }.count(1)
            method { name = "contains";param(StringClass) }.count(1)
            method { returnType = AutoCloseable::class.java }.count(1)
            method { param(StringClass, IntType);returnType = IntClass }.count(1)
            method { param(StringClass, LongType);returnType = LongClass }.count(1)
            method { param(StringClass, StringClass);returnType = StringClass }.count(1)
            method { param(StringClass, BooleanType);returnType = BooleanClass }.count(1)
        }.get()?.hook {
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
        } ?: loggerD(msg = "$packageName\nError -> ReplaceOnePlusModelWaterMark")
    }
}