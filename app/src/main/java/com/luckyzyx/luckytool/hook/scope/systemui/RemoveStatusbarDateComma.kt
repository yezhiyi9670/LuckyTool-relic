package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveStatusbarDateComma : YukiBaseHooker() {
    override fun onHook() {
        //Source WeatherInfoParseHelper
        //cn_comma
        findClass("com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper").hook {
            injectMember {
                method {
                    name = "getChineseDateInfo"
                    paramCount = 2
                }
                afterHook {
                    result = result.toString().replace("，"," ")
                }
            }
        }
    }
}