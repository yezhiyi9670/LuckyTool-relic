package com.luckyzyx.luckytool.hook.scope.oplusmms

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookMMSFeatureOption : YukiBaseHooker() {
    override fun onHook() {
        //移除快捷验证码悬浮窗 -> oplus.software.inputmethod.verify_code_enable
        val removeVerifyCode =
            prefs(ModulePrefs).getBoolean("remove_verification_code_floating_window", false)

        //Source FeatureOption.java
        //com.oplus.common -> C12
        DexkitUtils.searchDexClass(
            "HookMMSFeatureOption", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(BooleanType.name)
                    }
                    methods {
                        add { paramTypes(ContextClass.name) }
                        add { paramTypes(StringClass.name) }
                        add { paramTypes(StringClass.name, BooleanType.name) }
                        add { paramTypes(ContextClass.name, StringClass.name) }
                        add { paramTypes(ContextClass.name, StringClass.name, StringClass.name) }
                        add { paramTypes(ContextClass.name);returnType(UnitType.name) }
                        add { paramTypes(ContextClass.name);returnType(BooleanType.name) }
                        add { paramCount(0);returnType(BooleanType.name) }
                    }
                    usingStrings("FeatureOption")
                }
            }
        }?.firstOrNull()?.className?.hook {
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
        }
    }
}