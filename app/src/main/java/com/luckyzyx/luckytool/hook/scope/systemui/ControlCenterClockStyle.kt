package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A12
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object ControlCenterClockStyle : YukiBaseHooker() {
    override fun onHook() {
        val showSecond =
            prefs(ModulePrefs).getBoolean("control_center_clock_show_second", false)
        val removeRedOne =
            prefs(ModulePrefs).getBoolean("remove_control_center_clock_red_one", false)
        val fixColon = prefs(ModulePrefs).getBoolean("fix_clock_colon_style", false)
        //Source Clock
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method {
                    name = "setShowSecondsAndUpdate"
                }
                beforeHook {
                    if (showSecond) args(0).setTrue()
                }
            }
            if (SDK >= A12) return@hook
            injectMember {
                method {
                    name = "setTextWithOpStyle"
                    paramCount = 1
                }
                afterHook {
                    val view = instance<TextView>()
                    val char = args(0).cast<CharSequence>() ?: return@afterHook
                    setRedOneStyle(view, StringBuilder(char), removeRedOne)
                }
            }
        }
        if (SDK < A12) return
        //Source BaseClockExt
        findClass("com.oplusos.systemui.ext.BaseClockExt").hook {
            injectMember {
                method {
                    name = "setTextWithRedOneStyleInternal"
                    paramCount = 2
                }
                replaceUnit {
                    val view = args(0).cast<TextView>() ?: return@replaceUnit
                    val char = args(1).cast<CharSequence>() ?: return@replaceUnit
                    val sb = fixColonStyle(char, fixColon)
                    setRedOneStyle(view, sb, removeRedOne)
                }
            }
        }
    }

    private fun fixColonStyle(char: CharSequence, fixColon: Boolean): StringBuilder {
        var sb = StringBuilder(char)
        if (fixColon) return sb
        for (i in char.indices) {
            if (sb[i].toString() == ":") {
                sb = sb.replace(i, i + 1, "\u200e\u2236")
                break
            }
        }
        return sb
    }

    private fun setRedOneStyle(view: TextView, sb: StringBuilder, remove: Boolean) {
        if (remove) {
            view.text = sb
            return
        }
        val sp = SpannableString(sb)
        for (i2 in 0 until 2) {
            if (sb[i2].toString() == "1") {
                sp.setSpan(ForegroundColorSpan(Color.parseColor("#c41442")), i2, i2 + 1, 0)
            }
        }
        view.setText(sp, TextView.BufferType.SPANNABLE)
    }
}