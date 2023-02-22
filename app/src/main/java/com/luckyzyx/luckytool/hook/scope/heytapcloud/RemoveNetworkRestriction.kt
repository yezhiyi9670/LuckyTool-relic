package com.luckyzyx.luckytool.hook.scope.heytapcloud

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType

object RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source BackupRestoreHelper -> String backup_currently_mobile
        //Source NetworkUtil
        //Search getSystemService -> connectivity
        //Search Const.Callback.NetworkState.NetworkType.NETWORK_MOBILE -> ? 1 : 0 -> Method
        searchClass {
            from("com.cloud.base.commonsdk.baseutils", "t2").absolute()
            constructor().none()
            field().count(0..1)
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