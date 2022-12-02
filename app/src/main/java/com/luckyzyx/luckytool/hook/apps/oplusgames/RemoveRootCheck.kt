package com.luckyzyx.luckytool.hook.apps.oplusgames

import android.os.Bundle
import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringType
import com.luckyzyx.luckytool.utils.data.XposedPrefs
import java.util.*

class RemoveRootCheck : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val code = appSet[1].toInt()
        if (code >= 7016000) {
            val clazz = when (code) {
                7016000 -> "br.g.n"
                else -> "RootCheckClass"
            }
            findClass(clazz).hook {
                injectMember {
                    method {
                        emptyParam()
                        returnType = BundleClass
                        order().index().first()
                    }
                    afterHook {
                        result<Bundle>()?.putInt("isSafe", 0)
                    }
                }
            }
            return
        }
        //Source COSASDKManager
        //Search -> dynamic_feature_cool_ex / getSupportCoolEx -> Method
        //isSafe:null; -> isSafe:0
        searchClass {
            from("com.oplus.x", "com.oplus.f", "com.oplus.a0", "yp", "hr").absolute()
            field { type = StringType }.count(5..6)
            field { type = BooleanType }.count(2..3)
            field { type = IntType }.count(1..2)
            method {
                emptyParam()
                returnType = BundleClass
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BundleClass
                }
                afterHook {
                    result<Bundle>()?.putInt("isSafe", 0)
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveRootCheck")
    }
}
