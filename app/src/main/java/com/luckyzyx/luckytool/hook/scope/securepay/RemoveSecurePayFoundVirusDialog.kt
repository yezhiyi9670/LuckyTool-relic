package com.luckyzyx.luckytool.hook.scope.securepay

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.CheckBoxClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.DialogInterfaceClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType

object RemoveSecurePayFoundVirusDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source RiskDialogWrapper
        searchClass {
            from("com.coloros.securepay").absolute()
            field { type = CheckBoxClass }.count(1)
            field { type = BooleanType }.count(1)
            constructor().count(1)
            method { emptyParam() }.count(5..6)
            method { emptyParam();returnType = BooleanType }.count(2)
            method { param(ContextClass, StringClass);returnType = BooleanType }.count(1)
            method {
                param(ContextClass, StringClass, IntType, DialogInterfaceClass, IntType)
            }.count(2)
        }.get()?.hook {
            injectMember {
                method { param { it[1] == StringClass };paramCount = 2;returnType = UnitType }
                intercept()
            }
            injectMember {
                method { emptyParam();returnType = UnitType }.all()
                intercept()
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveSecurePayFoundVirusDialog")
    }
}