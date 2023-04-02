package com.luckyzyx.luckytool.hook.scope.phonemanager

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass

object RemoveVirusRiskNotificationInPhoneManager : YukiBaseHooker() {
    override fun onHook() {
        //Source VirusScanNotifyListener
        searchClass {
            from("l6", "m6", "n6").absolute()
            field {
                type = ContextClass
            }.count(1)
            field {
                type = StringClass
            }.count(1)
            constructor {
                param(ContextClass, StringClass)
            }.count(1)
            method {
                emptyParam()
                returnType = IntType
            }.count(1)
            method {
                param(StringClass)
                returnType = StringClass
            }.count(1)
            method {
                param(ArrayListClass)
            }.count(2)
        }.get()?.hook {
            injectMember {
                method {
                    param(ArrayListClass)
                }.all()
                intercept()
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveVirusRiskNotificationInPhoneManager")
    }
}