package com.luckyzyx.luckytool.hook.scope.oplusmms

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringArrayClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookMMSFeatureOption : YukiBaseHooker() {
    override fun onHook() {
        //移除快捷验证码悬浮窗 -> oplus.software.inputmethod.verify_code_enable
        val removeVerifyCode =
            prefs(ModulePrefs).getBoolean("remove_verification_code_floating_window", false)

        //Source FeatureOption.java
        searchClass {
            //com.oplus.common -> C12
            from(
                "com.oplus.mms.foundation.libcompat",
                "zd","ie"
            )
            field { type = BooleanType }.count { it > 40 }
            field { type = StringArrayClass }.count(3)
            method { param(StringClass);returnType = BooleanType }.count(1)
            method { param(StringClass, BooleanType);returnType = BooleanType }.count(1)
            method { param(ContextClass);returnType = BooleanType }.count(8)
            method { param(ContextClass, StringArrayClass) }.count(1)
            method { param(ContextClass, StringClass);returnType = BooleanType }.count(3)
            method {
                param(ContextClass, StringClass, StringClass)
                returnType = BooleanType
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    param(ContextClass, StringClass, StringClass)
                    returnType = BooleanType
                }
                beforeHook {
                    val key = args(1).cast<String>()
                    val key2 = args(2).cast<String>()
                    if (key == null) {
                        when (key2) {
                            "oplus.software.inputmethod.verify_code_enable" -> if (removeVerifyCode) resultFalse()
                        }
                    }
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> HookMMSFeatureOption")
    }
}