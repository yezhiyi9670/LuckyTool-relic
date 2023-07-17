package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType

object ShowChargingRipple : YukiBaseHooker() {
    override fun onHook() {
        //Sourcee FeatureFlags
        //flag_charging_ripple
        findClass("com.android.systemui.statusbar.FeatureFlags").hook {
            injectMember {
                method { name = "isChargingRippleEnabled" }
                replaceToTrue()
            }
        }
        //Source WiredChargingRippleController
        findClass("com.android.systemui.statusbar.charging.WiredChargingRippleController").hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook {
                    field { name = "rippleEnabled" }.get(instance).setTrue()
                }
            }
        }
    }
}