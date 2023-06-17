package com.luckyzyx.luckytool.hook.scope.heytapcloud

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType

object RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source BackUpActivity / BackupRestoreHelper -> backup_currently_mobile
        //Source BackupRestoreCode -> BackupRestoreCode.NO_WIFI
        //Source NetworkUtil -> 2 == ?() -> getSystemService -> connectivity
        //Search Const.Callback.NetworkState.NetworkType.NETWORK_MOBILE -> ? 1 : 0 -> Method
        searchClass {
            from(
                "com.cloud.base.commonsdk.baseutils",
                "qa", "t2", "ra", "ob", "mb"
            ).absolute()
            method { emptyParam();returnType = IntType }.count(1..2)
            method { param(IntType);returnType = BooleanType }.count(4..5)
            method { param(ContextClass);returnType = BooleanType }.count(2..5)
            method { returnType = IntType }.count(2..4)
            method { returnType = BooleanType }.count(7..10)
        }.get()?.hook {
            injectMember {
                method { emptyParam();returnType = IntType }.all()
                afterHook { if (result<Int>() == 1) result = 2 }
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveNetworkRestriction")
    }
}