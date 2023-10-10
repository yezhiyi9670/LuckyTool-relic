package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.constructor
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.hasField
import com.highcapable.yukihookapi.hook.factory.method

object VibrateWhenOpeningTheStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //Source PanelViewController -> config_vibrateOnIconAnimation
        VariousClass(
            "com.android.systemui.statusbar.phone.PanelViewController", //C13
            "com.android.systemui.shade.NotificationPanelViewController" //C14
        ).toClass().apply {
            constructor().hook {
                after { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
            }
        }

        //Source StatusBarCommandQueueCallbacks -> config_vibrateOnIconAnimation
        VariousClass(
            "com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks", //C13
            "com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks" //C14
        ).toClass().apply {
            if (hasField { name = "mVibrateOnOpening" }.not()) return@apply
            constructor().hook {
                after { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
            }
        }

        //Source PanelViewController -> config_vibrateOnIconAnimation
        "com.android.systemui.statusbar.phone.StatusBar".toClassOrNull()?.apply {
            if (hasField { name = "mVibrateOnOpening" }.not()) return@apply
            method { name = "start" }.hook {
                after { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
            }
        }
    }
}