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
import com.luckyzyx.luckytool.utils.ModulePrefs

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
            //Source SysFeatureUtils
            searchClass {
                from("oi", "ki", "ji", "vf", "uf", "qf", "mi", "ni", "qi").absolute()
                field { type = BooleanClass }.count { it > 30 }
                method { emptyParam();returnType = BooleanType }.count { it > 30 }
                method { param(ContextClass);returnType = BooleanType }.count(5..6)
                method { param(StringClass);returnType = BooleanType }.count(2)
            }.get()?.hook {
                injectMember {
                    method { param(StringClass);returnType = BooleanType }.all()
                    beforeHook {
                        when (args().first().string()) {
                            //Source Iris5SettingsFragment -> iris5_motion_fluency_optimization_switch
                            "oplus.software.video.rm_memc" -> if (memcVideo) resultFalse()
                            "oplus.software.display.pixelworks_enable" -> if (memcVideo) resultTrue()
                        }
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> HookSysFeature")
        }
    }

    private object HookAppFeatureProvider : YukiBaseHooker() {
        override fun onHook() {
            val isDisableCN =
                prefs(ModulePrefs).getBoolean("disable_cn_special_edition_setting", false)
            val neverTimeout = prefs(ModulePrefs).getBoolean("enable_show_never_timeout", false)
            //Source AppFeatureProviderUtils
            searchClass {
                from("ma", "rb", "yb", "sb", "la").absolute()
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
                        }
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> HookAppFeatureProvider")
        }
    }

    private object HookExpUst : YukiBaseHooker() {
        override fun onHook() {
            val neverTimeout = prefs(ModulePrefs).getBoolean("enable_show_never_timeout", false)
            //Source ExpUstUtils
            searchClass {
                from("oi", "ki", "ji", "vf", "uf", "qf", "mi", "ni", "qi").absolute()
                field().count(1)
                method { param(StringClass);returnType = ApplicationInfoClass }.count(1)
                method {
                    param(CharSequenceArrayClass);returnType = CharSequenceArrayClass
                }.count(2)
                method { emptyParam();returnType = StringClass }.count(2)
                method { emptyParam();returnType = BooleanType }.count(4..5)
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
                            11 -> if (neverTimeout) resultTrue()
                        }
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> HookExpUst")
        }
    }
}