package com.luckyzyx.luckytool.hook.scope.phonemanager

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils

object RemoveVirusRiskNotificationInPhoneManager : YukiBaseHooker() {
    override fun onHook() {
        //Source VirusScanNotifyListener
        DexkitUtils.searchDexClass(
            "RemoveVirusRiskNotificationInPhoneManager", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType(StringClass.name)
                    }
                    methods {
                        add { paramTypes(ArrayListClass.name) }
                        add { returnType(IntType.name) }
                        add { returnType(StringClass.name) }
                    }
                    usingStrings("VirusScanNotifyListener")
                }
            }
        }.toClass().apply {
            method { param(ArrayListClass) }.hookAll {
                intercept()
            }
        }
    }
}