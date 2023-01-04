package com.luckyzyx.luckytool.hook.scope.heytapcloud

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
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
        findClass("com.oplus.nearx.track.internal.utils.NetworkUtil").hook {
            injectMember {
                method {
                    param(ContextClass)
                    paramCount = 1
                    returnType = IntType
                }
                afterHook {
                    if (result<Int>() == 1) result = 2
                }
            }
        }
        if (code >= 60500) return
        //Source NetworkUtil
        //Search getSystemService -> connectivity
        //Search Const.Callback.NetworkState.NetworkType.NETWORK_MOBILE -> ? 1 : 0 -> Method
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
                emptyParam()
                returnType = IntType
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
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