package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object RemoveMobileDataIcon : YukiBaseHooker() {
    override fun onHook() {
        val removeIcon = prefs(ModulePrefs).getBoolean("remove_mobile_data_icon", false)
        val removeInout = prefs(ModulePrefs).getBoolean("remove_mobile_data_inout", false)
        val removeType = prefs(ModulePrefs).getBoolean("remove_mobile_data_type", false)
        if (!(removeIcon || removeInout || removeType)) return
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