package com.luckyzyx.luckytool.hook.apps.heytapcloud

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringType
import com.luckyzyx.luckytool.utils.data.XposedPrefs
import java.util.*

class RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val code = appSet[1].toInt()
        if (code >= 70500) {
            searchClass {
                from("com.oplus.nearx.track.internal.utils").absolute()
                constructor().count(1)
                field().count(3)
                method {
                    param(ContextClass)
                }.count(4)
                method {
                    param(IntType)
                    returnType = IntType
                }.count(1)
                method {
                    param(ContextClass)
                    returnType = IntType
                }.count(1)
                method {
                    param(ContextClass)
                    returnType = StringType
                }.count(1)
                method {
                    param(ContextClass)
                    returnType = BooleanType
                }.count(2)
            }.get()?.hook {
                injectMember {
                    method {
                        param(ContextClass)
                        returnType = IntType
                    }
                    afterHook {
                        if (result<Int>() == 1) result = 2
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> RemoveNetworkRestriction")
            return
        }
        //Source NetworkUtil
        //Search -> equalsIgnoreCase Const.Callback.NetworkState.NetworkType.NETWORK_MOBILE -> ? 1 : 0 -> Method
        searchClass {
            from("com.cloud.base.commonsdk.baseutils", "t2").absolute()
            constructor().none()
            field().none()
            method().count(13..14)
            method {
                param(ContextClass)
            }.count(6..7)
            method {
                param(IntType)
            }.count(4..5)
            method {
                modifiers { isPublic && isStatic }
                emptyParam()
                returnType = IntType
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    modifiers { isPublic && isStatic }
                    emptyParam()
                    returnType = IntType
                }
                afterHook {
                    if (result<Int>() == 1) result = 2
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveNetworkRestriction")
    }
}