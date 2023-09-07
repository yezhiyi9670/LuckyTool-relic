package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.highcapable.yukihookapi.hook.factory.hasField

object VibrateWhenOpeningTheStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //Source PanelViewController -> config_vibrateOnIconAnimation
        VariousClass(
            "com.android.systemui.statusbar.phone.PanelViewController", //C13
            "com.android.systemui.shade.NotificationPanelViewController" //C14
        ).hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
            }
        }

        //Source StatusBarCommandQueueCallbacks -> config_vibrateOnIconAnimation
        VariousClass(
            "com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks", //C13
            "com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks" //C14
        ).getOrNull(appClassLoader)?.let {
            if (it.hasField { name = "mVibrateOnOpening" }.not()) return@let
            it.hook {
                injectMember {
                    allMembers(MembersType.CONSTRUCTOR)
                    afterHook { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
                }
            }
        }

        //Source PanelViewController -> config_vibrateOnIconAnimation
        "com.android.systemui.statusbar.phone.StatusBar".toClass().let {
            if (it.hasField { name = "mVibrateOnOpening" }.not()) return@let
            it.hook {
                injectMember {
                    method { name = "start" }
                    afterHook { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
                }
            }
        }
    }
}