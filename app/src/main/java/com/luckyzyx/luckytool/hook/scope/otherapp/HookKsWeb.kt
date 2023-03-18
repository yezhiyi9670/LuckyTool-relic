package com.luckyzyx.luckytool.hook.scope.otherapp

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.SharedPreferencesClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookKsWeb : YukiBaseHooker() {
    override fun onHook() {
        val isPro = prefs(ModulePrefs).getBoolean("ksweb_remove_check_license", false)
        //Source EXTEND TO PRO VERSION / CHECK SERIAL KEY / KSWEB PRO / KSWEB STANDARD
        searchClass {
            from("b4").absolute()
            field {
                type = IntType
            }.count(1)
            field {
                type = BooleanType
            }.count(1)
            field {
                type = SharedPreferencesClass
            }.count(1)
            constructor().count(2)
            method {
                emptyParam()
                returnType = IntType
            }.count(1)
            method {
                emptyParam()
                returnType = BooleanType
            }.count(3..5)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BooleanType
                }.all()
                beforeHook {
                    if (!isPro) return@beforeHook
                    field {
                        type = BooleanType
                    }.get(instance).setTrue()
                    field {
                        type = IntType
                    }.get(instance).set(2)
                }
            }
        }
    }
}