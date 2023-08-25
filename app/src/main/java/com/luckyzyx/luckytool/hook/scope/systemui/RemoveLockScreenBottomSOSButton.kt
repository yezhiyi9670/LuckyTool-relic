package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveLockScreenBottomSOSButton : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusEmergencyButtonControllExImpl
        VariousClass(
            "com.oplus.systemui.keyguard.OplusEmergencyButtonControllExImpl", //C13
            "com.oplus.keyguard.OplusEmergencyButtonExImpl" //C14
        ).hook {
            injectMember {
                method { name = "shouldUpdateEmergencyCallButton" }
                beforeHook {
                    field { name = "mEmergencyButton" }.get(instance).cast<View>()
                        ?.isVisible = false
                    resultTrue()
                }
            }
        }
    }
}