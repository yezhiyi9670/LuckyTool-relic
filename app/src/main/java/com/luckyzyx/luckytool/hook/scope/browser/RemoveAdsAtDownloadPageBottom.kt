package com.luckyzyx.luckytool.hook.scope.browser

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.printLog

object RemoveAdsAtDownloadPageBottom : YukiBaseHooker() {
    override fun onHook() {
        //Source AppRecommendManager -> LinearLayout setVisibility 0/8 500L
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findMethod {
                matcher {
                    paramCount(0)
                    returnType(UnitType.name)
                    usingNumbers(0, 8, 500L)
                    addUsingField {
                        matcher {
                            addPutMethod {
                                paramTypes("com.heytap.browser.downloads.entity.RecommendConfig")
                                returnType(UnitType.name)
                            }
                            type("com.heytap.browser.downloads.entity.RecommendConfig")
                        }
                    }
                    addUsingField {
                        matcher {
                            addPutMethod {
                                paramCount(0)
                                returnType(UnitType.name)
                            }
                            type("android.widget.LinearLayout")
                        }
                    }
                    addUsingField {
                        matcher {
                            addPutMethod {
                                paramCount(0)
                                returnType(UnitType.name)
                            }
                            type("com.coui.appcompat.tablayout.COUITabLayout")
                        }
                    }
                }
            }.apply {
                printLog("RemoveAdsAtDownloadPageBottom")
                if (isNullOrEmpty().not() && size == 1) {
                    val member = firstOrNull() ?: return@create
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