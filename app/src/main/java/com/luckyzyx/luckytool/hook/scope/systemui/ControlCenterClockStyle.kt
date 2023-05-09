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
        val showSecond =
            prefs(ModulePrefs).getBoolean("control_center_clock_show_second", false)
        var redOneMode =
            prefs(ModulePrefs).getString("statusbar_control_center_clock_red_one_mode", "0")
        dataChannel.wait<String>("statusbar_control_center_clock_red_one_mode") { redOneMode = it }
        var colonStyle =
            prefs(ModulePrefs).getString("statusbar_control_center_clock_colon_style", "0")
        dataChannel.wait<String>("statusbar_control_center_clock_colon_style") { colonStyle = it }

        //Source Clock
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method { name = "setShowSecondsAndUpdate" }
                beforeHook {
                    val view = instance<TextView>()
                    if (view.context.resources.getResourceEntryName(view.id) != "qs_footer_clock") return@beforeHook
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
                    if (view.context.resources.getResourceEntryName(view.id) != "qs_footer_clock") return@afterHook
                    val char = args(0).cast<CharSequence>() ?: return@afterHook
                    setStyle(view, char, "0", redOneMode)
                }
            }
        }
        //Source BaseClockExt
        val clazz = "com.oplusos.systemui.ext.BaseClockExt"
        if (clazz.toClassOrNull() == null) return
        findClass(clazz).hook {
            injectMember {
                method {
                    name = "setTextWithRedOneStyle"
                    paramCount = 2
                }
                afterHook {
                    val view = args(0).cast<TextView>() ?: return@afterHook
                    if (view.context.resources.getResourceEntryName(view.id) != "qs_footer_clock") return@afterHook
                    val char = args(1).cast<CharSequence>() ?: return@afterHook
                    setStyle(view, char, colonStyle, redOneMode)
                }
            }
        }
    }

    private fun getCharColor(view: TextView): Int? {
        val sp = SpannableString(view.text)
        val colorSpan = sp.getSpans(0, sp.length, ForegroundColorSpan::class.java)
        return if (colorSpan.isNotEmpty()) colorSpan[0].foregroundColor else null
    }

    private fun setStyle(view: TextView, char: CharSequence, colonStyle: String, redStyle: String) {
        val colonMode = if (colonStyle == "1") 1 else if (colonStyle == "2") 2 else 0
        val redMode = if (redStyle == "1") 1 else if (redStyle == "2") 2 else 0
        var sb = StringBuilder(view.text)
        if (colonMode != 0) {
            when (colonMode) {
                1 -> sb = StringBuilder(char)
                2 -> for (i in char.indices) {
                    if (sb[i].toString() == ":") {
                        sb = sb.replace(i, i + 1, "\u200e\u2236")
                    }
                }
            }
        }
        if (redMode != 2) {
            val sp = SpannableString(sb)
            for (i2 in 0 until 2) {
                if (sb[i2].toString() == "1") {
                    when (redMode) {
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