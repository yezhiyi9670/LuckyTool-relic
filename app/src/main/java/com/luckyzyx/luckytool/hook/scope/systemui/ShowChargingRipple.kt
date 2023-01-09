package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object ShowChargingRipple : YukiBaseHooker() {
    override fun onHook() {
        //flag_charging_ripple
        resources().hook {
            injectResource {
                conditions {
                    name = "flag_charging_ripple"
                    bool()
                }
                replaceToTrue()
            }
        }
    }
}