package com.luckyzyx.luckytool.hook.scope.oplusgames

import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitPrefs
import com.luckyzyx.luckytool.utils.ModulePrefs
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

object RemoveRootCheck : YukiBaseHooker() {
    const val key = "remove_root_check"

    override fun onHook() {
        //Source COSASDKManager
        //Search getSupportCoolEx new Bundle -> Class
        //Search getFeature -> dynamic_feature_cool_ex
        //isSafe:null; -> isSafe:0
        val isEnable = prefs(ModulePrefs).getBoolean(key, false)
        if (!isEnable) return

        val clsName = prefs(DexkitPrefs).getString(key, "null")
        //Source OtherSystemStorage
        findClass(clsName).hook {
            injectMember {
                method { emptyParam();returnType = BundleClass }
                afterHook { result<Bundle>()?.putInt("isSafe", 0) }
            }
        }
    }

    fun searchDexkit(appPath: String): ClassDataList {
        var result = ClassDataList()
        DexKitBridge.create(appPath)?.use { bridge ->
            result = bridge.findClass {
                searchPackages = listOf(
                    "com.oplus.cosa", "com.oplus.x", "com.oplus.f", "com.oplus.a0",
                    "yp", "hr", "br", "ir"
                )
                matcher {
                    fields {
                        addForType(StringClass.name)
                        addForType(BooleanType.name)
                        addForType(IntType.name)
                    }
                    methods {
                        add { name = "clear";paramCount = 0 }
                        add { paramCount = 0;returnType = BundleClass.name }
                    }
                }
            }
        }
        return result
    }
}
