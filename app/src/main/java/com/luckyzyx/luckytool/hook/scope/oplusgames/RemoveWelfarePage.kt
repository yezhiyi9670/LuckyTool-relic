package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.UnitType

object RemoveWelfarePage : YukiBaseHooker() {
    override fun onHook() {
        //Source MainPanelView
        findClass("business.mainpanel.MainPanelView").hook {
            injectMember {
                method {
                    param { it[0] == ListClass && it[1] == BooleanType }
                    paramCount(2..3)
                    returnType = UnitType
                }
                beforeHook {
                    val list = args().first().list<Any>().toMutableList()
                    list.removeLastOrNull() ?: return@beforeHook
                    if (list.size >= 2) loggerD(msg = "$packageName\nError -> RemoveWelfarePage")
                    args().first().set(java.util.ArrayList(list))
                }
            }
        }
    }
}