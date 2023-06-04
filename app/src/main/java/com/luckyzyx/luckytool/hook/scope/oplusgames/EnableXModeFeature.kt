package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.AnyClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object EnableXModeFeature : YukiBaseHooker() {
    override fun onHook() {
        //Source CoolingBackClipHelper
        findClass("business.module.perfmode.CoolingBackClipHelper").hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BooleanType
                }.all()
                replaceToTrue()
            }
            injectMember {
                method {
                    paramCount = 1
                    returnType = AnyClass
                }
                afterHook { resultTrue() }
            }
        }
    }
}