package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass

object EnableAdrenoGpuController : YukiBaseHooker() {
    override fun onHook() {
        //Source GpuSettingHelper
        //Search isSupportGpuControlPanel
        searchClass {
            from("fc", "gc", "ec", "x7", "w7").absolute()
            field {
                type = ListClass
            }.count(5)
            method {
                param(StringClass)
            }.count(5)
            method {
                param(StringClass)
                returnType = BooleanType
            }.count(4)
            method {
                param(ContextClass, StringClass)
            }.count(2)
            method {
                emptyParam()
                returnType = BooleanType
            }.count(3)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType(BooleanType).index().last()
                }
                replaceToTrue()
            }
        } ?: loggerD(msg = "$packageName\nError -> EnableAdrenoGpuController")
    }
}