package com.luckyzyx.luckytool.hook.scope.browser

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils

object RemoveAdsAtDownloadPageBottom : YukiBaseHooker() {
    override fun onHook() {
        //Source AppRecommendManager
        DexkitUtils.searchDexClass("RemoveAdsAtDownloadPageBottom", appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
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
            }
        }?.firstOrNull()?.className?.toClassOrNull()?.hook {
            injectMember {
                method { param("com.heytap.browser.downloads.entity.RecommendConfig") }
                beforeHook { args().first().setNull() }
            }
        }
    }
}