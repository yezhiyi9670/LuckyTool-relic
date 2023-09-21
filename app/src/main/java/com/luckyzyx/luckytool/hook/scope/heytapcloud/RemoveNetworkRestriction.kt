package com.luckyzyx.luckytool.hook.scope.heytapcloud

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils

object RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source BackUpActivity / BackupRestoreHelper -> backup_currently_mobile
        //Source WifiCheck -> check -> BackupRestoreCode -> NO_WIFI / SUCCESS
        //Source NetworkUtil -> 2 == ?() -> getSystemService -> connectivity
        //Search Const.Callback.NetworkState.NetworkType.NETWORK_MOBILE -> ? 1 : 0 -> Method
        DexkitUtils.searchDexClass(
            "RemoveNetworkRestriction", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    methods {
                        add { paramTypes(IntType.name) }
                        add { paramTypes(ContextClass.name) }
                        add { returnType(IntType.name) }
                        add { returnType(BooleanType.name) }
                        add { returnType(StringClass.name) }
                        add { returnType(UnitType.name) }
                    }
                    usingStrings(
                        "NetworkUtil",
                        "connectivity",
                        "getNetworkTypeString",
                        "isMobileDataNetwork",
                        "isNetworkConnected"
                    )
                }
            }
        }?.firstOrNull()?.className?.hook {
            injectMember {
                method { emptyParam();returnType = IntType }.all()
                afterHook { if (result<Int>() == 1) result = 2 }
            }
        }
    }
}