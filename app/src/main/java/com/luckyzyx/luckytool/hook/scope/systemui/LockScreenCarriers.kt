package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.ModulePrefs

object LockScreenCarriers : YukiBaseHooker() {
    override fun onHook() {
        val userFont = prefs(ModulePrefs).getBoolean("statusbar_carriers_use_user_typeface", false)
        val isRemove = prefs(ModulePrefs).getBoolean("remove_statusbar_carriers", false)
        //Source StatOperatorNameView
        findClass("com.oplusos.systemui.statusbar.widget.StatOperatorNameView").hook {
            injectMember {
                constructor { paramCount = 3 }
                afterHook { if (userFont) instance<TextView>().typeface = Typeface.DEFAULT_BOLD }
            }
            injectMember {
                method { name = "onConfigurationChanged" }
                afterHook { if (userFont) instance<TextView>().typeface = Typeface.DEFAULT_BOLD }
            }
            injectMember {
                method {
                    name = "updateCarrierInfo"
                    superClass()
                }
                afterHook { if (isRemove) instance<TextView>().isVisible = false }
            }
        }
    }
}