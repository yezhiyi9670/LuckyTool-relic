package com.luckyzyx.luckytool.hook.scope.systemui

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object ControlCenterClockStyle : YukiBaseHooker() {
    override fun onHook() {
        var showSecond =
            prefs(ModulePrefs).getBoolean("control_center_clock_show_second", false)
        dataChannel.wait<Boolean>("control_center_clock_show_second") { showSecond = it }
        var redOneMode =
            prefs(ModulePrefs).getString("statusbar_control_center_clock_red_one_mode", "0")
        dataChannel.wait<String>("statusbar_control_center_clock_red_one_mode") { redOneMode = it }
        var fixColon = prefs(ModulePrefs).getBoolean("fix_clock_colon_style", false)
        dataChannel.wait<Boolean>("fix_clock_colon_style") { fixColon = it }

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
                    setStyle(view, char, true, redOneMode)
                }
            }
        }
        //Source BaseClockExt
        "com.oplusos.systemui.ext.BaseClockExt".toClassOrNull()?.hook {
            injectMember {
                method {
                    name = "setTextWithRedOneStyle"
                    paramCount = 2
                }
                afterHook {
                    val view = args(0).cast<TextView>() ?: return@afterHook
                    val char = args(1).cast<CharSequence>() ?: return@afterHook
                    setStyle(view, char, fixColon, redOneMode)
                }
            }
        }
    }

    private fun getCharColor(view: TextView): Int? {
        val sp = SpannableString(view.text)
        val colorSpan = sp.getSpans(0, sp.length, ForegroundColorSpan::class.java)
        return if (colorSpan.isNotEmpty()) {
            colorSpan[0].foregroundColor
        } else null
    }

    private fun setStyle(view: TextView, char: CharSequence, fixColon: Boolean, redMode: String) {
        var sb = StringBuilder(char)
        if (!fixColon) {
            for (i in char.indices) {
                if (sb[i].toString() == ":") {
                    sb = sb.replace(i, i + 1, "\u200e\u2236")
                }
            }
        }
        val mode = when (redMode) {
            "1" -> 1
            "2" -> 2
            else -> 0
        }
        if (mode != 2) {
            val sp = SpannableString(sb)
            for (i2 in 0 until 2) {
                if (sb[i2].toString() == "1") {
                    when (mode) {
                        0 -> {
                            val color = getCharColor(view)
                            if (color != null) sp.setSpan(
                                ForegroundColorSpan(color),
                                i2, i2 + 1, 0
                            )
                        }

                        1 -> sp.setSpan(
                            ForegroundColorSpan(Color.parseColor("#c41442")),
                            i2, i2 + 1, 0
                        )
                    }
                }
            }
            view.setText(sp, TextView.BufferType.SPANNABLE)
        } else view.text = sb
    }
}