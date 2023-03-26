package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Typeface
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object CarriersUseUserTypeface : YukiBaseHooker() {
    override fun onHook() {
        //Source StatOperatorNameView
        findClass("com.oplusos.systemui.statusbar.widget.StatOperatorNameView").hook {
            injectMember {
                constructor {
                    paramCount = 3
                }
                afterHook {
                    instance<TextView>().typeface = Typeface.DEFAULT_BOLD
                }
            }
            injectMember {
                method {
                    name = "onConfigurationChanged"
                }
                intercept()
            }
        }
    }
}