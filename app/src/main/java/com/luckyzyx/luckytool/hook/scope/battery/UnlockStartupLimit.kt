package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.java.AnyClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils

object UnlockStartupLimit : YukiBaseHooker() {

    override fun onHook() {
        //Source StartupManager.java
        //Search -> ? 5 : 20; -> Method
        DexkitUtils.searchDexClass(
            "UnlockStartupLimit", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(AnyClass.name)
                        addForType(ContextClass.name)
                        count(4..6)
                    }
                    methods {
                        add { paramCount(0);returnType(IntType.name) }
                        add { paramTypes(IntentClass.name);returnType(UnitType.name) }
                        add { paramTypes(BundleClass.name);returnType(UnitType.name) }
                    }
                }
            }
        }?.firstOrNull()?.className?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(999)
            }
        }
    }
}