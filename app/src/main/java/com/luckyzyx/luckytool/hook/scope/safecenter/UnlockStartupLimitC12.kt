package com.luckyzyx.luckytool.hook.scope.safecenter

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType

object UnlockStartupLimitC12 : YukiBaseHooker() {
    override fun onHook() {
        //Source StratupManager
        //Search -> auto_start_max_allow_count -> update max allow count
        VariousClass(
            "com.coloros.safecenter.startupapp.b",
            "com.oplus.safecenter.startupapp.a"
        ).hook {
            injectMember {
                method {
                    param(ContextClass)
                    returnType = UnitType
                }.all()
                afterHook {
                    field { type = IntType }.get().set(10000)
                }
            }
        }
    }
}