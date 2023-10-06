package com.luckyzyx.luckytool.hook.scope.browser

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.printLog

object RemoveAdsAtDownloadPageBottom : YukiBaseHooker() {
    override fun onHook() {
        //Source AppRecommendManager
        DexkitUtils.create(appInfo.sourceDir) { dexkitBridge ->
            val clsList = dexkitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType(BooleanType.name)
                        addForType("java.util.List")
                        addForType("com.heytap.nearx.uikit.widget.NearTabLayout")
                        addForType("com.heytap.browser.downloads.entity.RecommendConfig")
                    }
                    methods {
                        add { paramCount(0);returnType(UnitType.name) }
                        add { paramCount(0);returnType(BooleanType.name) }
                        add { returnType("com.heytap.nearx.uikit.widget.NearTabLayout") }
                        add { returnType("com.heytap.browser.downloads.entity.RecommendConfig") }
                        add {
                            paramTypes("com.heytap.browser.downloads.entity.RecommendConfig")
                            returnType(UnitType.name)
                        }
                    }
                    usingStrings("AppRecommendManager")
                }
            }.printLog("RemoveAdsAtDownloadPageBottom")
            if (clsList.isNullOrEmpty().not() && clsList?.size == 1) {
                val methodList = dexkitBridge.findMethod {
                    searchPackages(clsList.first().className)
                    matcher {
                        paramCount(0)
                        returnType(UnitType.name)
                        usingNumbers(0, 8, 500L)
                    }
                }.printLog("RemoveAdsAtDownloadPageBottom")
                if (methodList.isNullOrEmpty().not() && methodList?.size == 1) {
                    val member = methodList.firstOrNull() ?: return@create
                    member.className.toClass().apply {
                        method {
                            name = member.methodName
                            emptyParam()
                            returnType(UnitType)
                        }.hook {
                            replaceUnit {
                                field { type("android.widget.LinearLayout") }.get(instance)
                                    .cast<View>()?.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }
}