package com.luckyzyx.luckytool.hook.scope.systemui

import android.telephony.SubscriptionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object RemoveMobileDataIcon : YukiBaseHooker() {
    override fun onHook() {
        val removeIcon = prefs(ModulePrefs).getBoolean("remove_mobile_data_icon", false)
        val removeInout = prefs(ModulePrefs).getBoolean("remove_mobile_data_inout", false)
        val removeType = prefs(ModulePrefs).getBoolean("remove_mobile_data_type", false)
        val hideUnused = prefs(ModulePrefs).getBoolean("hide_unused_card_icons", false)
        //Source OplusStatusBarMobileViewExImpl
        VariousClass(
            //mobile_type
            "com.oplusos.systemui.statusbar.OplusStatusBarMobileView",
            "com.oplus.systemui.statusbar.phone.signal.OplusStatusBarMobileViewExImpl"
        ).hook {
            injectMember {
                method {
                    name = "initViewState"
                }
                afterHook {
                    if (hideUnused) {
                        val state = args().first().any()
                        val subId = state?.current()?.field { name = "subId" }?.int()
                        val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                        field { name = "mMobileGroup" }.get(instance).cast<ViewGroup>()?.isVisible =
                            subId == subId2
                    }
                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
                        .cast<ViewGroup>()?.isVisible = false
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
                    if (hideUnused) {
                        val state = args().first().any()
                        val subId = state?.current()?.field { name = "subId" }?.int()
                        val subId2 = SubscriptionManager.getDefaultDataSubscriptionId()
                        field { name = "mMobileGroup" }.get(instance).cast<ViewGroup>()?.isVisible =
                            subId == subId2
                    }
                    if (removeIcon) field { name = "mMobileGroup" }.get(instance)
                        .cast<ViewGroup>()?.isVisible = false
                    if (removeInout) field { name = "mDataActivity" }.get(instance)
                        .cast<View>()?.isVisible = false
                    if (removeType) field { name = "mMobileType" }.get(instance)
                        .cast<View>()?.isVisible = false
                }
            }
        }
    }
}