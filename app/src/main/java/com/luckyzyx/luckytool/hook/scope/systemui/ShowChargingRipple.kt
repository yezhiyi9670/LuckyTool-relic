package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

object ShowChargingRipple : YukiBaseHooker() {
    override fun onHook() {
        //Source WiredChargingRippleController -> flag_charging_ripple
        VariousClass(
            "com.android.systemui.statusbar.charging.WiredChargingRippleController", //C13
            "com.android.systemui.charging.WiredChargingRippleController" //C14
        ).toClass().apply {
            constructor().hook {
                after {
                    field { name = "rippleEnabled" }.get(instance).setTrue()
                }
            }

        }
        if (SDK >= A14) return
        //Sourcee FeatureFlags -> flag_charging_ripple
        "com.android.systemui.statusbar.FeatureFlags".toClass().apply {
            method { name = "isChargingRippleEnabled" }.hook {
                replaceToTrue()
            }
        }
    }
}