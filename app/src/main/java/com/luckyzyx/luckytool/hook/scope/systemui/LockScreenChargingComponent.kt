package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.allViews
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.TextViewClass
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object LockScreenChargingComponent : YukiBaseHooker() {
    override fun onHook() {
        val userTypeface = prefs(ModulePrefs).getBoolean("lock_screen_use_user_typeface", false)
        //Source ChargingLevelAndLogoView
        findClass("com.oplusos.systemui.keyguard.charginganim.siphonanim.ChargingLevelAndLogoView").hook {
            injectMember {
                method {
                    name = "updatePowerFormat"
                    paramCount = 1
                }
                afterHook {
                    if (!userTypeface) return@afterHook
                    instance<LinearLayout>().allViews.forEach {
                        if (it.javaClass == TextViewClass) {
                            (it as TextView).typeface = Typeface.DEFAULT_BOLD
                        }
                    }
                }
            }
        }
    }
}