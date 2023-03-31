package com.luckyzyx.luckytool.hook.scope.systemui

import android.telephony.SubscriptionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object MobileDataIconRelated : YukiBaseHooker() {
    override fun onHook() {
//        val removeIcon = prefs(ModulePrefs).getBoolean("remove_mobile_data_icon", false)
        val removeInout = prefs(ModulePrefs).getBoolean("remove_mobile_data_inout", false)
        val removeType = prefs(ModulePrefs).getBoolean("remove_mobile_data_type", false)
        var hideNonNetwork = prefs(ModulePrefs).getBoolean("hide_non_network_card_icon", false)
        dataChannel.wait<Boolean>("hide_non_network_card_icon") { hideNonNetwork = it }
        var hideNoSS = prefs(ModulePrefs).getBoolean("hide_nosim_noservice", false)
        dataChannel.wait<Boolean>("hide_nosim_noservice") { hideNoSS = it }
        //Source OplusStatusBarMobileViewExImpl
        VariousClass(
            "com.oplusos.systemui.statusbar.OplusStatusBarMobileView", //C12
            "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarMobileViewExImpl" //C13
        ).hook {
            injectMember {
                method {
                    name = "initViewState"
                }
                afterHook {
                    if (hideNonNetwork) {
                        val state = args().first().any()
                        val subId = state?.current()?.field { name = "subId" }?.int()
                        val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                        field { name = "mMobileGroup" }.get(instance).cast<ViewGroup>()?.isVisible =
                            subId == subId2
                    }
//                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
//                        .cast<ViewGroup>()?.isVisible = false
                    if (removeInout) field { name = "mDataActivity" }.get(instance)
                        .cast<View>()?.isVisible = false
                    if (removeType) field { name = "mMobileType" }.get(instance)
                        .cast<View>()?.isVisible = false
                }
            }
            injectMember {
                method {
                    name = when (instanceClass.simpleName) {
                        "OplusStatusBarMobileView" -> "updateMobileViewState"
                        "OplusStatusBarMobileViewExImpl" -> "updateState"
                        else -> "updateState"
                    }
                }
                afterHook {
                    if (hideNonNetwork) {
                        val state = args().first().any()
                        val subId = state?.current()?.field { name = "subId" }?.int()
                        val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                        field { name = "mMobileGroup" }.get(instance).cast<ViewGroup>()?.isVisible =
                            subId == subId2
                    }
//                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
//                        .cast<ViewGroup>()?.isVisible = false
                    if (removeInout) field { name = "mDataActivity" }.get(instance)
                        .cast<View>()?.isVisible = false
                    if (removeType) field { name = "mMobileType" }.get(instance)
                        .cast<View>()?.isVisible = false
                }
            }
        }

        //Source OplusStatusBarSignalPolicyExImpl
        VariousClass(
            "com.oplusos.systemui.ext.StatusBarSignalPolicyExt", //C12
            "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarSignalPolicyExImpl" //C13
        ).hook {
            injectMember {
                method {
                    name = "setNoSims"
                    paramCount = 3
                }
                afterHook {
                    if (!hideNoSS) return@afterHook
                    val iconController =
                        if (SDK >= A13) method { name = "getIconController" }.get(instance).call()
                        else field { name = "iconController" }.get(instance).any()
                    val slotNoSim = field { name = "slotNoSim" }.get(instance).cast<String>()
                    iconController?.current()?.method {
                        name = "setIconVisibility"
                    }?.call(slotNoSim, false)
                }
            }
        }
    }
}