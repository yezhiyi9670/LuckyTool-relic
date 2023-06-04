package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass

object EnableSupportCompetitionMode : YukiBaseHooker() {
    override fun onHook() {
        //Source CompetitionModeManager
        //Search isSupportCompetitionMode
        searchClass {
            //714,715,716
            from("od", "rd", "pd", "com.coloros.gamespaceui.module.gamefocus").absolute()
            field { type = ListClass }.count(1)
            method {
                emptyParam()
                returnType = ListClass
            }.count(1..2)
            method { param(StringClass, ArrayListClass) }.count(1)
            method {
                emptyParam()
                returnType = BooleanType
            }.count(6)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BooleanType
                    order().index(2)
                }
                replaceToTrue()
            }
        } ?: loggerD(msg = "$packageName\nError -> EnableSupportCompetitionMode")
    }
}