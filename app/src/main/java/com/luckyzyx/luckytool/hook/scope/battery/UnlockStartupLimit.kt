package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType

object UnlockStartupLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source StartupManager.java
        //Search -> ? 5 : 20; -> Method
        searchClass {
            from("i7", "q7", "u7", "y7", "s7", "z8", "b9", "t7", "r7").absolute()
            field().count(4)
            field { type = ContextClass }.count(1)
            constructor { param(ContextClass) }.count(1)
            method {
                emptyParam()
                returnType = IntType
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(999)
            }
        } ?: loggerD(msg = "$packageName\nError -> UnlockStartupLimit")
    }
}