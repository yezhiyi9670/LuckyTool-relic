package com.luckyzyx.luckytool.hook.scope.opluslinker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

class ForceDisplayCommunicationSharing(private val appSet: Array<String>) : YukiBaseHooker() {
    override fun onHook() {
        val version = appSet[1].toIntOrNull() ?: return
        if (version < 14000000) loadHooker(CommunicationSharingV13)
        else loadHooker(CommunicationSharingV14)
    }

    private object CommunicationSharingV13 : YukiBaseHooker() {
        override fun onHook() {
            //Source ConnectPCSettingsActivity -> ConnectPCSettingsFragment -> key virtualmodem_share
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

    private object CommunicationSharingV14 : YukiBaseHooker() {
        override fun onHook() {
            //Search isVirtualCommSupport / isAppExist
            findClass("com.oplus.linker.synergy.util.Utils").hook {
                injectMember {
                    method { name = "isVirtualized" }
                    replaceToTrue()
                }
            }
        }
    }
}