package com.luckyzyx.luckytool.hook.scope.heytapcloud

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass

object RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source BackUpActivity / BackupRestoreHelper -> backup_currently_mobile
        //Source BackupRestoreCode -> BackupRestoreCode.NO_WIFI
        //Source NetworkUtil getSystemService -> connectivity
        //Search Const.Callback.NetworkState.NetworkType.NETWORK_MOBILE -> ? 1 : 0 -> Method
        searchClass {
            from(
                "com.cloud.base.commonsdk.baseutils",
                "qa",
                "t2",
                "ra",
                "ob",
                "mb"
            ).absolute()
            field().count(0..1)
            method {
                param(ContextClass)
                returnType = BooleanType
            }.count(2..5)
            method {
                param(ContextClass)
                returnType = StringClass
            }.count(0..1)
            method {
                param(IntType)
                returnType = BooleanType
            }.count(4..5)
            method {
                emptyParam()
                returnType = IntType
            }.count(1..2)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }.all()
                afterHook { if (result<Int>() == 1) result = 2 }
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveNetworkRestriction")
    }
}