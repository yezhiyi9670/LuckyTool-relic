package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import android.text.TextUtils
import android.util.LayoutDirection
import android.view.ViewGroup
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.hook.utils.LunarHelperUtils
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.getScreenStatus
import java.util.Locale

@Suppress("LocalVariableName", "DiscouragedApi")
object RemoveControlCenterDateComma : YukiBaseHooker() {
    override fun onHook() {
        var removeComma = prefs(ModulePrefs).getBoolean("remove_control_center_date_comma", false)
        dataChannel.wait<Boolean>("remove_control_center_date_comma") { removeComma = it }
        var showLunar =
            prefs(ModulePrefs).getBoolean("statusbar_control_center_date_show_lunar", false)
        dataChannel.wait<Boolean>("statusbar_control_center_date_show_lunar") { showLunar = it }
        var fixDate = prefs(ModulePrefs).getBoolean("fix_control_center_date_display", false)
        dataChannel.wait<Boolean>("fix_control_center_date_display") { fixDate = it }
        //Source WeatherInfoParseHelper -> cn_comma
        findClass("com.oplusos.systemui.keyguard.clock.WeatherInfoParseHelper").hook {
            injectMember {
                method {
                    name = "getChineseDateInfo"
                    paramCount = 2
                }
                afterHook {
                    if (removeComma) result = result<String>()?.replace("，", " ")
                    if (showLunar) {
                        val context = args().last().cast<Context>() ?: return@afterHook
                        val lunarInstance = LunarHelperUtils(appClassLoader).buildInstance(context)
                        val lunarDate = LunarHelperUtils(appClassLoader).getDateToString(
                            lunarInstance, System.currentTimeMillis()
                        ).let {
                            if ((it.isNullOrBlank()) || (it.length < 8)) ""
                            else "\n" + it.substring(4, it.length)
                        }
                        result = result<String>() + lunarDate
                    }
                }
            }
        }

        if (SDK < A13) return
        //Source OplusQSFooterImpl
        findClass("com.oplusos.systemui.qs.OplusQSFooterImpl").hook {
            injectMember {
                method {
                    name = "updateQsDateView"
                }
                afterHook {
                    if (!fixDate) return@afterHook
                    val mTmpConstraintSet = field { name = "mTmpConstraintSet" }.get(instance).any()
                        ?: return@afterHook
                    val mClockView = field { name = "mClockView" }.get(instance).cast<TextView>()
                        ?: return@afterHook
                    val mQsDateView = field { name = "mQsDateView" }.get(instance).cast<TextView>()
                        ?: return@afterHook

                    mTmpConstraintSet.current().method {
                        name = "constrainWidth"
                    }.call(mQsDateView.id, -2)

                    if (showLunar) {
                        val res = instance<ViewGroup>().resources
                        val qs_footer_date_margin_start = res.getDimensionPixelSize(
                            res.getIdentifier(
                                "qs_footer_date_margin_start", "dimen",
                                packageName
                            )
                        )
                        val qs_footer_date_expand_translation_x = res.getDimensionPixelSize(
                            res.getIdentifier(
                                "qs_footer_date_expand_translation_x", "dimen",
                                packageName
                            )
                        )
                        val qs_footer_date_expand_translation_y = res.getDimensionPixelSize(
                            res.getIdentifier(
                                "qs_footer_date_expand_translation_y", "dimen",
                                packageName
                            )
                        )
                        val isRtl =
                            TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL
                        val width = mClockView.width + qs_footer_date_margin_start
                        val translationX =
                            if (!isRtl) (-width) + qs_footer_date_expand_translation_x else width
                        val translationY = qs_footer_date_expand_translation_y / 2

                        getScreenStatus(res) {
                            if (it) return@getScreenStatus
                            mTmpConstraintSet.current().method {
                                name = "setTranslationX"
                            }.call(mQsDateView.id, translationX.toFloat())
                            mTmpConstraintSet.current().method {
                                name = "setTranslationY"
                            }.call(mQsDateView.id, translationY.toFloat())
                        }
                    }
                }
            }
        }
    }
}