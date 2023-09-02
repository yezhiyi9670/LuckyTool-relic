package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object LockScreenCarriers : YukiBaseHooker() {
    override fun onHook() {
        if (SDK >= A14) loadHooker(LockScreenCarrierV14)
        else loadHooker(LockScreenCarrierV13)
    }

    private object LockScreenCarrierV14 : YukiBaseHooker() {
        override fun onHook() {
            val userFont =
                prefs(ModulePrefs).getBoolean("statusbar_carriers_use_user_typeface", false)
            val isRemove = prefs(ModulePrefs).getBoolean("remove_statusbar_carriers", false)

            //Source OplusStatCarrierTextController
            findClass("com.oplus.systemui.statusbar.widget.OplusStatCarrierTextController").hook {
                injectMember {
                    method { name = "onViewAttached" }
                    afterHook {
                        if (isRemove) field { name = "mView";superClass() }.get(instance)
                            .cast<TextView>()?.isVisible = false
                    }
                }
                injectMember {
                    method { name = "setVisible" }
                    beforeHook { if (isRemove) args().first().setFalse() }
                }
                injectMember {
                    method { name = "updateCarrierInfo" }
                    afterHook {
                        if (isRemove) field { name = "mView";superClass() }.get(instance)
                            .cast<TextView>()?.isVisible = false
                    }
                }
            }

            //Source OplusStatCarrierText
            findClass("com.oplus.systemui.statusbar.widget.OplusStatCarrierText").hook {
                injectMember {
                    constructor { paramCount = 2 }
                    afterHook {
                        if (userFont) instance<TextView>().typeface = Typeface.DEFAULT_BOLD
                    }
                }
                injectMember {
                    method { name = "onConfigurationChanged" }
                    afterHook {
                        if (userFont) instance<TextView>().typeface = Typeface.DEFAULT_BOLD
                    }
                }
            }
        }
    }

    private object LockScreenCarrierV13 : YukiBaseHooker() {
        override fun onHook() {
            val userFont =
                prefs(ModulePrefs).getBoolean("statusbar_carriers_use_user_typeface", false)
            val isRemove = prefs(ModulePrefs).getBoolean("remove_statusbar_carriers", false)
            //Source StatOperatorNameView
            findClass("com.oplusos.systemui.statusbar.widget.StatOperatorNameView").hook {
                injectMember {
                    constructor { paramCount = 3 }
                    afterHook {
                        if (userFont) instance<TextView>().typeface = Typeface.DEFAULT_BOLD
                    }
                }
                injectMember {
                    method { name = "onConfigurationChanged" }
                    afterHook {
                        if (userFont) instance<TextView>().typeface = Typeface.DEFAULT_BOLD
                    }
                }
                injectMember {
                    method { name = "updateCarrierInfo";superClass() }
                    afterHook { if (isRemove) instance<TextView>().isVisible = false }
                }
            }
        }
    }
}