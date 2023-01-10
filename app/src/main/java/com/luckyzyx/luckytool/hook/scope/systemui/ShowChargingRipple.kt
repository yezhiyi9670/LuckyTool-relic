package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object ShowChargingRipple : YukiBaseHooker() {
    override fun onHook() {
        //Sourcee FeatureFlags
        //flag_charging_ripple
        findClass("com.android.systemui.statusbar.FeatureFlags").hook {
            injectMember {
                method {
                    name = "isChargingRippleEnabled"
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}