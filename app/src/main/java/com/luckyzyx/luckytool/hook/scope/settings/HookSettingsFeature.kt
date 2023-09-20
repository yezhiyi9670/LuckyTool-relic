package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ApplicationInfoClass
import com.highcapable.yukihookapi.hook.type.android.ContentResolverClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceArrayClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookSettingsFeature : YukiBaseHooker() {
    override fun onHook() {
        loadHooker(HookSysFeature)
        loadHooker(HookAppFeatureProvider)
        loadHooker(HookExpUst)
    }

    private object HookSysFeature : YukiBaseHooker() {
        override fun onHook() {
            val memcVideo = false
            //prefs(ModulePrefs).getBoolean("force_display_video_memc_frame_insertion", false)
            val rgbPalette =
                prefs(ModulePrefs).getBoolean("enable_screen_color_temperature_rgb_palette", false)

            //Source SysFeatureUtils
            searchClass {
                from(
                    "com.oplus.settings.utils",
                    "oi", "ki", "ji", "vf", "uf", "qf", "mi", "ni", "qi", "li", "pi", "om", "km",
                    "ri", "zi", "nm", "uf", "aj"
                ).absolute()
                field { type = BooleanClass }.count { it > 30 }
                method { emptyParam();returnType = BooleanType }.count { it > 70 }
                method { param(ContextClass);returnType = BooleanType }.count(5..7)
                method { param(StringClass);returnType = BooleanType }.count(2)
            }.get()?.hook {
                injectMember {
                    method { param(StringClass);returnType = BooleanType }.all()
                    beforeHook {
                        when (args().first().string()) {
                            //Source Iris5SettingsFragment -> iris5_motion_fluency_optimization_switch
                            "oplus.software.video.rm_memc" -> if (memcVideo) resultFalse()
                            "oplus.software.display.pixelworks_enable" -> if (memcVideo) resultTrue()
                            //Source ColorModeFragment -> oplus.software.display.rgb_ball_support
                            "oplus.software.display.rgb_ball_support" -> if ((rgbPalette)) resultTrue()
                        }
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> HookSysFeature")
        }
    }

    private object HookExpUst : YukiBaseHooker() {
        override fun onHook() {
            val neverTimeout = prefs(ModulePrefs).getBoolean("enable_show_never_timeout", false)

            //Source ExpUstUtils
            searchClass {
                from(
                    "com.oplus.settings.utils",
                    "oi", "ki", "ji", "vf", "uf", "qf", "mi", "ni", "qi", "li", "pi", "om", "km",
                    "ri", "zi", "nm", "uf", "aj"
                ).absolute()
                method { param(StringClass);returnType = ApplicationInfoClass }.count(1)
                method {
                    param(CharSequenceArrayClass);returnType = CharSequenceArrayClass
                }.count(2)
                method { emptyParam();returnType = StringClass }.count(2..3)
                method { emptyParam();returnType = BooleanType }.count(4..8)
                method { param(StringClass);returnType = BooleanType }.count(2..3)
                method { param(IntType);returnType = BooleanType }.count(3)
                method { param(ContextClass, IntType) }.count(1)
                method { param(IntType);returnType = StringClass }.count(1)
                method { param(IntType, StringClass);returnType = StringClass }.count(1)
                method { param(StringClass);returnType = StringClass }.count(1)
                method { param(StringClass, StringClass);returnType = StringClass }.count(1)
            }.get()?.hook {
                injectMember {
                    method { param(IntType);returnType = BooleanType }.all()
                    beforeHook {
                        when (args().first().int()) {
                            //Source DisplayTimeOutController -> 永不息屏(24H)
                            11 -> if (SDK < A13 && neverTimeout) resultTrue()
                        }
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> HookExpUst")
        }
    }

    private object HookAppFeatureProvider : YukiBaseHooker() {
        override fun onHook() {
            val isDisableCN =
                prefs(ModulePrefs).getBoolean("disable_cn_special_edition_setting", false)
            val neverTimeout = prefs(ModulePrefs).getBoolean("enable_show_never_timeout", false)
            val processorDetail = prefs(ModulePrefs).getString("set_processor_click_page", "0")
            val processManagement =
                prefs(ModulePrefs).getBoolean("force_display_process_management", false)

            //Source AppFeatureProviderUtils
            searchClass {
                from(
                    "com.oplus.coreapp.appfeature",
                    "ma", "rb", "yb", "sb", "la", "ub", "kf", "gf", "zb", "if"
                ).absolute()
                method {
                    param(ContentResolverClass, StringClass, BooleanType)
                    returnType = BooleanType
                }.count(1)
                method {
                    param(ContentResolverClass, StringClass, IntType)
                    returnType = IntType
                }.count(1)
                method {
                    param(ContentResolverClass, StringClass, StringClass)
                    returnType = StringClass
                }.count(1..2)
                method {
                    param(ContentResolverClass, StringClass)
                    returnType = ListClass
                }.count(1..2)
                method {
                    param(ContentResolverClass, StringClass)
                    returnType = BooleanType
                }.count(1)
            }.get()?.hook {
                injectMember {
                    method { param(ContentResolverClass, StringClass);returnType = BooleanType }
                    beforeHook {
                        when (args().last().string()) {
                            //Source OplusDefaultAutofillPicker -> autofill_password 自动填充密码
                            "com.android.settings.cn_version" -> if (isDisableCN) resultFalse()
                            //Source DisplayTimeOutController -> 永不息屏(24H)
                            "com.android.settings.show_never_timeout" -> if (neverTimeout) resultTrue()
                            //com.android.settings.processor_detail
                            "com.android.settings.processor_detail" -> if (processorDetail != "0") resultTrue()
                            //com.android.settings.processor_detail_gen2
                            "com.android.settings.processor_detail_gen2" -> if (processorDetail == "2") resultTrue()
                            //com.android.settings.ultimate_cleanup
                            "com.android.settings.ultimate_cleanup" -> if (processManagement) resultTrue()
                        }
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> HookAppFeatureProvider")
        }
    }
}