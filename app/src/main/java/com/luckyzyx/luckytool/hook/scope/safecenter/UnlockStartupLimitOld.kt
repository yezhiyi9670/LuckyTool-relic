package com.luckyzyx.luckytool.hook.scope.safecenter

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.SDK

object UnlockStartupLimitOld : YukiBaseHooker() {

    override fun onHook() {
        val clazz = if (SDK < A12) "com.coloros.safecenter.startupapp.b"
        else "com.oplus.safecenter.startupapp.a"
        //Source StartupManager.java
        //Search -> auto_start_max_allow_count -> update max allow count
        findClass(clazz).hook {
            injectMember {
                method {
                    param(ContextClass)
                    returnType = UnitType
                }.all()
                afterHook { field { type = IntType }.get().set(10000) }
            }
        }
    }
}