package com.luckyzyx.luckytool.hook.scope.permissioncontroller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils

object UnlockDefaultDesktopLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source FeatureOption -> oplus.software.defaultapp.remove_force_launcher
        DexkitUtils.searchDexClass(
            "UnlockDefaultDesktopLimit", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(BooleanType.name)
                    }
                    methods {
                        add { paramCount(1) }
                        add { paramCount(0);returnType(BooleanType.name) }
                        add { paramTypes(ContextClass.name);returnType(UnitType.name) }
                    }
                    usingStrings(
                        "oplus.software.pms_app_frozen",
                        "oplus.software.defaultapp.remove_force_launcher",
                        "oplus.hardware.type.tablet",
                        "persist.sys.permission.enable"
                    )
                }
            }
        }?.firstOrNull()?.className?.toClass()?.apply {
            method { param(ContextClass) }.hook {
                after { field { type(BooleanType).index(1) }.get().setTrue() }
            }
        }
    }
}