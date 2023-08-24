package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.highcapable.yukihookapi.hook.factory.hasField
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.SDK

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
        ).hook {
            if (instanceClass.hasField { name = "mVibrateOnOpening" }) {
                injectMember {
                    allMembers(MembersType.CONSTRUCTOR)
                    afterHook { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
                }
            }
        }
        if (SDK >= A14) return
        //Source PanelViewController -> config_vibrateOnIconAnimation
        findClass("com.android.systemui.statusbar.phone.StatusBar").hook {
            injectMember {
                method { name = "start" }
                afterHook { field { name = "mVibrateOnOpening" }.get(instance).setTrue() }
            }
        }
    }
}