package com.luckyzyx.luckytool.hook.scope.oplusgames

import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils
import org.luckypray.dexkit.query.ClassDataList

object RemoveRootCheck : YukiBaseHooker() {
    override fun onHook() {
        //Source COSASDKManager
        //Search getSupportCoolEx new Bundle -> Class
        //Search getFeature -> dynamic_feature_cool_ex
        //isSafe:null; -> isSafe:0
        val clsName = searchDexkit(appInfo.sourceDir).firstOrNull()?.className
            ?: "null"
        //Source OtherSystemStorage
        findClass(clsName).hook {
            injectMember {
                method { emptyParam();returnType = BundleClass }
                afterHook { result<Bundle>()?.putInt("isSafe", 0) }
            }
        }
    }

    private fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexkitUtils.create(appPath)?.use { bridge ->
            result = bridge.findClass {
                matcher {
                    fields {
                        addForType(StringClass.name)
                        addForType(BooleanType.name)
                        addForType(IntType.name)
                    }
                    methods {
                        add { name = "clear";paramCount(0) }
                        add { paramCount(0);returnType(BundleClass.name) }
                    }
                }
            }
        }
        return result
    }
}
