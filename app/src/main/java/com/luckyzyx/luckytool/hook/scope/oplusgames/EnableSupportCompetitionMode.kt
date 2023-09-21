package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.printLog
import org.luckypray.dexkit.query.ClassDataList

object EnableSupportCompetitionMode : YukiBaseHooker() {
    override fun onHook() {
        //Source CompetitionModeManager
        //Search isSupportCompetitionMode
        val clsName = searchDexkit(appInfo.sourceDir).firstOrNull()?.className
            ?: "null"
        findClass(clsName).hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BooleanType
                    order().index(2)
                }
                replaceToTrue()
            }
        }
    }

    private fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexkitUtils.create(appPath)?.use { bridge ->
            result = bridge.findClass {
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
            }
        }
        result.printLog("EnableSupportCompetitionMode")
        return result
    }
}