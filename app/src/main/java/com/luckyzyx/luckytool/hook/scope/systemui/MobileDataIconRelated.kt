package com.luckyzyx.luckytool.hook.scope.systemui

import android.telephony.SubscriptionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object MobileDataIconRelated : YukiBaseHooker() {
    override fun onHook() {
        if (SDK >= A12) loadHooker(MobileDataIconRelated)
        else loadHooker(MobileDataIconRelatedC120)
    }

    object MobileDataIconRelated : YukiBaseHooker() {
        override fun onHook() {
            //        val removeIcon = prefs(ModulePrefs).getBoolean("remove_mobile_data_icon", false)
            val removeInout = prefs(ModulePrefs).getBoolean("remove_mobile_data_inout", false)
            val removeType = prefs(ModulePrefs).getBoolean("remove_mobile_data_type", false)
            var hideNonNetwork = prefs(ModulePrefs).getBoolean("hide_non_network_card_icon", false)
            dataChannel.wait<Boolean>("hide_non_network_card_icon") { hideNonNetwork = it }
            var hideNoSS = prefs(ModulePrefs).getBoolean("hide_nosim_noservice", false)
            dataChannel.wait<Boolean>("hide_nosim_noservice") { hideNoSS = it }

            //Source OplusStatusBarMobileViewExImpl -> initView
            VariousClass(
                "com.oplusos.systemui.statusbar.OplusStatusBarMobileView", //C12.1
                "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarMobileViewExImpl" //C13
            ).toClass().apply {
                method { name = "initViewState" }.hook {
                    after {
                        if (hideNonNetwork) {
                            val state = args().first().any()
                            val subId = state?.current()?.field { name = "subId" }?.int()
                            val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                            field { name = "mMobileGroup" }.get(instance)
                                .cast<ViewGroup>()?.isVisible =
                                subId == subId2
                        }
//                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
//                        .cast<ViewGroup>()?.isVisible = false
                        if (removeInout) field { name = "mDataActivity" }.get(instance)
                            .cast<View>()?.isVisible = false
                        if (removeType) field {
                            name = "mMobileType"
                            if (SDK < A13) superClass(true)
                        }.get(instance).cast<View>()?.isVisible = false
                    }
                }
                method {
                    name = when (simpleName) {
                        "OplusStatusBarMobileView" -> "updateMobileViewState"
                        "OplusStatusBarMobileViewExImpl" -> "updateState"
                        else -> "updateState"
                    }
                }.hook {
                    after {
                        if (hideNonNetwork) {
                            val state = args().first().any()
                            val subId = state?.current()?.field { name = "subId" }?.int()
                            val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                            field { name = "mMobileGroup" }.get(instance)
                                .cast<ViewGroup>()?.isVisible =
                                subId == subId2
                        }
//                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
//                        .cast<ViewGroup>()?.isVisible = false
                        if (removeInout) field { name = "mDataActivity" }.get(instance)
                            .cast<View>()?.isVisible = false
                        if (removeType) field {
                            name = "mMobileType"
                            if (SDK < A13) superClass(true)
                        }.get(instance).cast<View>()?.isVisible = false
                    }
                }
            }

            //Source OplusStatusBarSignalPolicyExImpl
            VariousClass(
                "com.oplusos.systemui.ext.StatusBarSignalPolicyExt", //C12.1
                "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarSignalPolicyExImpl" //C13
            ).toClass().apply {
                method {
                    name = "setNoSims"
                    paramCount = 3
                }.hook {
                    after {
                        if (!hideNoSS) return@after
                        val iconController = if (hasMethod { name = "getIconController" }) method {
                            name = "getIconController"
                        }.get(instance).call()
                        else field { name = "iconController" }.get(instance).any()
                        val slotNoSim = field { name = "slotNoSim" }.get(instance).cast<String>()
                        iconController?.current()?.method {
                            name = "setIconVisibility"
                            paramCount = 2
                            if (simpleName == "StatusBarSignalPolicyExt") superClass()
                        }?.call(slotNoSim, false)
                    }
                }
            }
        }
    }

    object MobileDataIconRelatedC120 : YukiBaseHooker() {
        override fun onHook() {
//        val removeIcon = prefs(ModulePrefs).getBoolean("remove_mobile_data_icon", false)
            val removeInout = prefs(ModulePrefs).getBoolean("remove_mobile_data_inout", false)
            val removeType = prefs(ModulePrefs).getBoolean("remove_mobile_data_type", false)
            var hideNonNetwork = prefs(ModulePrefs).getBoolean("hide_non_network_card_icon", false)
            dataChannel.wait<Boolean>("hide_non_network_card_icon") { hideNonNetwork = it }
            var hideNoSS = prefs(ModulePrefs).getBoolean("hide_nosim_noservice", false)
            dataChannel.wait<Boolean>("hide_nosim_noservice") { hideNoSS = it }

            //Source StatusBarMobileView
            "com.android.systemui.statusbar.StatusBarMobileView".toClass().apply {
                method { name = "initViewState" }.hook {
                    after {
                        if (hideNonNetwork) {
                            val state = field { name = "mState" }.get(instance).any()
                            val subId = state?.current()?.field { name = "subId" }?.int()
                            val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                            field { name = "mMobileGroup" }.get(instance)
                                .cast<ViewGroup>()?.isVisible =
                                subId == subId2
                        }
//                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
//                        .cast<ViewGroup>()?.isVisible = false
                        if (removeInout) field { name = "mInoutContainer" }.get(instance)
                            .cast<View>()?.isVisible = false
                        if (removeType) field { name = "mMobileType" }.get(instance)
                            .cast<View>()?.isVisible = false
                    }
                }
                method { name = "updateState" }.hook {
                    after {
                        if (hideNonNetwork) {
                            val state = field { name = "mState" }.get(instance).any()
                            val subId = state?.current()?.field { name = "subId" }?.int()
                            val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                            field { name = "mMobileGroup" }.get(instance)
                                .cast<ViewGroup>()?.isVisible =
                                subId == subId2
                        }
//                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
//                        .cast<ViewGroup>()?.isVisible = false
                        if (removeInout) field { name = "mInoutContainer" }.get(instance)
                            .cast<View>()?.isVisible = false
                        if (removeType) field { name = "mMobileType" }.get(instance)
                            .cast<View>()?.isVisible = false
                    }
                }
            }

            //Source SignalClusterView
            "com.oplusos.systemui.statusbar.widget.SignalClusterView".toClass().apply {
                method { name = "updateNoSimView" }.hook {
                    after {
                        if (!hideNoSS) return@after
                        val mNoSims = field { name = "mNoSims" }.get(instance).cast<View>()
                        method { name = "animateHide" }.get(instance).call(mNoSims, 8)
                    }
                }
            }
        }
    }
}

