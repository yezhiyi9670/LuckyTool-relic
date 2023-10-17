package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.checkDataList

object EnableSupportCompetitionMode : YukiBaseHooker() {
    override fun onHook() {
        //Source CompetitionModeManager
        //Search isSupportCompetitionMode
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ListClass.name)
                    }
                    methods {
                        add { paramCount(0);returnType(ListClass.name) }
                        add { paramCount(0);returnType(BooleanType.name) }
                        add { paramTypes(StringClass.name, ArrayListClass.name) }
                    }
                }
            }.apply {
                checkDataList("EnableSupportCompetitionMode")
                first().name.toClass().apply {
                    method {
                        emptyParam()
                        returnType = BooleanType
                        order().index(2)
                    }.hook {
                        replaceToTrue()
                    }
                }
            }
        }
    }
}