package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass

object RemoveWelfarePage : YukiBaseHooker() {
    override fun onHook() {
        //Source MainPanelView
        findClass("business.mainpanel.MainPanelView").hook {
            injectMember {
                method {
                    param(ListClass, BooleanType)
                }
                beforeHook {
                    val list = args().first().list<Any>()
                    val newlist = java.util.ArrayList<Any>().apply { add(list[0]) }
                    args().first().set(newlist)
                }
            }
        }
    }
}