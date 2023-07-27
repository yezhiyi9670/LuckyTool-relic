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
            from(
                "com.coloros.phonemanager.virusdetect.service",
                "l6", "m6", "n6", "e6", "q6"
            ).absolute()
            field { type = ContextClass }.count(1)
            field { type = StringClass }.count(1)
            constructor { paramCount(2..3) }.count(1)
            method { emptyParam();returnType = IntType }.count(1)
            method { param(StringClass);returnType = StringClass }.count(1)
            method { param(ArrayListClass) }.count(2)
        }.get()?.hook {
            injectMember {
                method { param(ArrayListClass) }.all()
                intercept()
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveVirusRiskNotificationInPhoneManager")
    }
}