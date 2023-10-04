package com.luckyzyx.luckytool.hook.scope.browser

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils

object RemoveAdsFromDownloadDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source DownloadCardAdProvider
        DexkitUtils.searchDexClass(
            "RemoveAdsFromDownloadDialog", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType(BooleanType.name)
                        addForType(StringClass.name)
                        addForType("com.opos.feed.api.FeedAdNative")
                        addForType("com.opos.feed.api.RecyclerAdHelper")
                        addForType("com.opos.feed.api.params.AdInteractionListener")
                    }
                    methods {
                        add { returnType("com.opos.feed.api.params.AdRequest") }
                        add { returnType("com.opos.feed.api.FeedAdNative") }
                        add { returnType("com.opos.feed.api.RecyclerAdHelper") }
                    }
                    usingStrings("DownloadCardAdProvider")
                }
            }
        }?.firstOrNull()?.className?.toClassOrNull()?.hook {
            injectMember {
                method {
                    paramCount(1)
                    returnType("com.opos.feed.api.params.AdRequest")
                }
                replaceTo(null)
            }
        }
    }
}