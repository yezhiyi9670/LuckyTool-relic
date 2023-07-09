package com.luckyzyx.luckytool.hook.scope.opluslinker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object ForceDisplayCommunicationSharing : YukiBaseHooker() {
    override fun onHook() {
        //Source ConnectPCSettingsFragment -> key virtualmodem_share
        findClass("com.oplus.linker.synergy.ui.fragment.ConnectPCSettingsFragment").hook {
            injectMember {
                method { name = "initPreference" }
                afterHook {
                    field { name = "mVirtualmodem" }.get(instance).any()?.current()?.method {
                        name = "setVisible"
                        superClass()
                    }?.call(true)
                }
            }
        }
    }
}