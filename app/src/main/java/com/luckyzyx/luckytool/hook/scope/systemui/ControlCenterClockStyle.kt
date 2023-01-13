package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.A12
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object ControlCenterClockStyle : YukiBaseHooker() {
    override fun onHook() {
        val showSecond =
            prefs(XposedPrefs).getBoolean("control_center_clock_show_second", false)
        val fixColon = prefs(XposedPrefs).getBoolean("fix_clock_colon_style", false)
        val removeRedOne =
            prefs(XposedPrefs).getBoolean("remove_control_center_clock_red_one", false)
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
                replaceUnit {
                    val view = instance<TextView>()
                    val char = args(0).cast<CharSequence>()!!
                    setRedOneStyle(view, char, removeRedOne)
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
                    val view = args(0).cast<TextView>()
                    val char = args(1).cast<CharSequence>()!!
                    setColonStyle(view, char, fixColon)
                    setRedOneStyle(view, char, removeRedOne)
                }
            }
        }
    }

    private fun setColonStyle(view: TextView?, char: CharSequence, fixColon: Boolean) {
        view?.text = if (fixColon) {
            char.toString().replace("\u200e\u2236", ":")
        } else {
            char.toString().replace(":", "\u200e\u2236")
        }
    }

    private fun setRedOneStyle(view: TextView?, char: CharSequence, removeRedOne: Boolean) {
        if (!removeRedOne) {
            val sp = SpannableString(view?.text)
            for (i in 0 until 2) {
                if (char[i].toString() == "1") {
                    val red = ForegroundColorSpan(Color.parseColor("#c41442"))
                    sp.setSpan(red, i, i + 1, 0)
                }
            }
            view?.setText(sp, TextView.BufferType.SPANNABLE)
        }
    }
}