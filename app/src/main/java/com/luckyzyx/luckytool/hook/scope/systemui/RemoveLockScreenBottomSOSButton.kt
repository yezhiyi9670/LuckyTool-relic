package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.factory.method

object RemoveLockScreenBottomSOSButton : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusEmergencyButtonControllExImpl
        VariousClass(
            "com.oplus.systemui.keyguard.OplusEmergencyButtonControllExImpl", //C13
            "com.oplus.keyguard.OplusEmergencyButtonExImpl" //C14
        ).toClass().apply {
            if (hasMethod { name = "disableShowEmergencyButton" }) method {
                name = "disableShowEmergencyButton"
            }.hook().replaceToTrue()
            else method { name = "shouldUpdateEmergencyCallButton" }.hook {
                before {
                    field { name = "mEmergencyButton" }.get(instance).cast<View>()
                        ?.isVisible = false
                    resultTrue()
                }
            }
        }
    }
}