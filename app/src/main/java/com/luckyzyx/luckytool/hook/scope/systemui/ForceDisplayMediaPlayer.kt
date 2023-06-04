package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object ForceDisplayMediaPlayer : YukiBaseHooker() {
    override fun onHook() {
        //Controller OplusQSContainerImplController -> onViewAttached -> onChanged
        //Source OplusQSContainerImpl
        findClass("com.oplusos.systemui.qs.OplusQSContainerImpl").hook {
            injectMember {
                method {
                    name = "setQsMediaPanelShown"
                    paramCount = 1
                }
                beforeHook { args().first().setTrue() }
            }
        }
        //Source QuickStatusBarHeader / OplusQSTileMediaContainerController
        VariousClass(
            "com.oplusos.systemui.qs.OplusQSTileMediaContainerController",
            "com.android.systemui.qs.QuickStatusBarHeader"
        ).hook {
            injectMember {
                method {
                    name = "setQsMediaPanelShown"
                    paramCount = 1
                }
                beforeHook { args(0).setTrue() }
            }
        }
        //Source OplusQSFooterImpl / OplusQSTileMediaContainer
        VariousClass(
            "com.oplusos.systemui.qs.OplusQSTileMediaContainer",
            "com.oplusos.systemui.qs.OplusQSFooterImpl"
        ).hook {
            injectMember {
                method {
                    name = "setMediaMode"
                    paramCount = 1
                }
                beforeHook { args(0).setTrue() }
            }
        }
    }
}