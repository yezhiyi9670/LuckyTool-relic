package com.luckyzyx.luckytool.hook.scope.permissioncontroller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType

object UnlockDefaultDesktopLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source FeatureOption -> oplus.software.defaultapp.remove_force_launcher
        searchClass {
            from(
                "com.oplusos.permissioncontroller.permission",
                "w4", "o6"
            )
            field { type = BooleanType }.count(3..4)
            method { param(ContextClass);returnType = UnitType }.count(1)
            method { emptyParam();returnType = BooleanType }.count(4..6)
        }.get()?.hook {
            injectMember {
                method { param(ContextClass) }
                afterHook { field { type(BooleanType).index(1) }.get().setTrue() }
            }
        }
    }
}