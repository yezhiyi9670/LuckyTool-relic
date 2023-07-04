package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.SDK

object VibrateWhenOpeningTheStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //Source PanelViewController -> config_vibrateOnIconAnimation
        findClass("com.android.systemui.statusbar.phone.PanelViewController").hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook {
                    field { name = "mVibrateOnOpening" }.get(instance).setTrue()
                }
            }
        }
        //Source PanelViewController -> config_vibrateOnIconAnimation
        findClass("com.android.systemui.statusbar.phone.StatusBar").hook {
            injectMember {
                method { name = "start" }
                afterHook {
                    field { name = "mVibrateOnOpening" }.get(instance).setTrue()
                }
            }
        }
        if (SDK < A13) return
        //Source StatusBarCommandQueueCallbacks -> config_vibrateOnIconAnimation
        findClass("com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks").hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook {
                    field { name = "mVibrateOnOpening" }.get(instance).setTrue()
                }
            }
        }

    }
}