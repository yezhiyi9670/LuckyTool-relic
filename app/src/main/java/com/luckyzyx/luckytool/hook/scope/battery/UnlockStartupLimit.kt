package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.java.AnyClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import org.luckypray.dexkit.query.ClassDataList

object UnlockStartupLimit : YukiBaseHooker() {

    override fun onHook() {
        val clsName = searchDexkit(appInfo.sourceDir).firstOrNull()?.className
            ?: "null"
        //Source StartupManager.java
        //Search -> ? 5 : 20; -> Method
        findClass(clsName).hook {
            injectMember {
                method { emptyParam();returnType = IntType }
                replaceTo(999)
            }
        }
    }

    private fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexkitUtils.create(appPath)?.use { bridge ->
            result = bridge.findClass {
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
        }
        return result
    }
}