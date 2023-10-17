package com.luckyzyx.luckytool.hook.scope.phonemanager

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.checkDataList

object RemoveVirusRiskNotificationInPhoneManager : YukiBaseHooker() {
    override fun onHook() {
        //Source VirusScanNotifyListener
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
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
            }.apply {
                checkDataList("RemoveVirusRiskNotificationInPhoneManager")
                first().name.toClass().apply {
                    method { param(ArrayListClass) }.hookAll {
                        intercept()
                    }
                }
            }
        }
    }
}