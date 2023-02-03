package com.luckyzyx.luckytool.hook.scope.systemui

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
        val fixColon = prefs(ModulePrefs).getBoolean("fix_clock_colon_style", false)
        val removeRedOne =
            prefs(ModulePrefs).getBoolean("remove_control_center_clock_red_one", false)
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
                    getCharColor(view) {
                        setRedOneStyle(view, char, it, removeRedOne)
                    }
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
                afterHook {
                    val view = args(0).cast<TextView>() ?: return@afterHook
                    val char = args(1).cast<CharSequence>() ?: return@afterHook
                    getCharColor(view) {
                        fixColonStyle(view, char, fixColon)
                        setRedOneStyle(view, char, it, removeRedOne)
                    }
                }
            }
        }
    }

    private fun getCharColor(view: TextView, result: (color: Int?) -> Unit) {
        val sp = SpannableString(view.text)
        val colorSpan = sp.getSpans(0, sp.length, ForegroundColorSpan::class.java)
        if (colorSpan.isNotEmpty()) {
            result(colorSpan[0].foregroundColor)
        } else result(null)
    }

    private fun fixColonStyle(view: TextView, char: CharSequence, fixColon: Boolean) {
        view.text = if (fixColon) {
            char.toString().replace("\u200e\u2236", ":")
        } else {
            char.toString().replace(":", "\u200e\u2236")
        }
    }

    private fun setRedOneStyle(view: TextView, char: CharSequence, color: Int?, remove: Boolean) {
        if (color == null) {
            view.text = char.toString()
        } else if (!remove) {
            val sp = SpannableString(view.text)
            for (i in 0 until 2) {
                if (char[i].toString() == "1") sp.setSpan(ForegroundColorSpan(color), i, i + 1, 0)
            }
            view.setText(sp, TextView.BufferType.SPANNABLE)
        }
    }
}