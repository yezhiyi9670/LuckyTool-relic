package com.luckyzyx.luckytool.hook.scope.securepay

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.CheckBoxClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.DialogInterfaceClass
import com.highcapable.yukihookapi.hook.type.android.ViewClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils

object RemoveSecurePayFoundVirusDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source RiskDialogWrapper
        DexkitUtils.searchDexClass(
            "RemoveSecurePayFoundVirusDialog", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                searchPackages("com.coloros.securepay")
                matcher {
                    fields {
                        addForType(BooleanType.name)
                        addForType(CheckBoxClass.name)
                    }
                    methods {
                        add { paramCount(0) }
                        add { paramTypes(ViewClass.name) }
                        add { returnType(UnitType.name) }
                        add { returnType(BooleanType.name) }
                        add {
                            paramTypes(
                                ContextClass.name,
                                StringClass.name,
                                IntType.name,
                                DialogInterfaceClass.name,
                                IntType.name
                            )
                        }
                    }
                    usingStrings("BROADCAST_SECURITY", "OPPO_COMPONENT_SAFE")
                }
            }
        }?.firstOrNull()?.className?.hook {
            injectMember {
                method {
                    param(VagueType, StringClass)
                    returnType = UnitType
                }
                intercept()
            }
            injectMember {
                method {
                    emptyParam()
                    returnType = UnitType
                }.all()
                intercept()
            }
        }
    }
}