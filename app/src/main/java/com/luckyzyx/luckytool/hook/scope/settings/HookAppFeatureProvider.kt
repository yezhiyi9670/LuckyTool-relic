package com.luckyzyx.luckytool.hook.scope.settings

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContentResolverClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookAppFeatureProvider : YukiBaseHooker() {

    override fun onHook() {
        val isDisableCN = prefs(ModulePrefs).getBoolean("disable_cn_special_edition_setting", false)
        //Source AppFeatureProviderUtils
        searchClass {
            from("ma", "rb", "yb").absolute()
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
                method {
                    param(ContentResolverClass, StringClass)
                    returnType = BooleanType
                }
                beforeHook {
                    when (args().last().string()) {
                        //Source OplusDefaultAutofillPicker -> autofill_password 自动填充密码
                        "com.android.settings.cn_version" -> if (isDisableCN) resultFalse()
                    }
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> HookAppFeatureProvider")
    }
}