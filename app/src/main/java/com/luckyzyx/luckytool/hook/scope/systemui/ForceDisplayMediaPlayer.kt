package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class ForceDisplayMediaPlayer : YukiBaseHooker() {
    override fun onHook() {
        //Controller OplusQSContainerImplController -> onViewAttached -> onChanged
        //Source OplusQSContainerImpl
        findClass("com.oplusos.systemui.qs.OplusQSContainerImpl").hook {
            injectMember {
                method {
                    name = "setQsMediaPanelShown"
                    paramCount = 1
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
        }
        //Source QuickStatusBarHeaderController
        findClass("com.android.systemui.qs.QuickStatusBarHeaderController").hook {
            injectMember {
                method {
                    name = "setQsMediaPanelShown"
                    paramCount = 1
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
        }
        //Source OplusQSFooterViewController
        findClass("com.oplusos.systemui.qs.OplusQSFooterViewController").hook {
            injectMember {
                method {
                    name = "setQsMediaPanelShown"
                    paramCount = 1
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
        }
    }
}