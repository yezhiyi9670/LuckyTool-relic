package com.luckyzyx.luckytool.hook.scope.camera

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.BitmapClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.PaintClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomModelWaterMark : YukiBaseHooker() {
    override fun onHook() {
        val isRealme = Build.MODEL.startsWith("RM", true)
        if (isRealme) return
        loadHooker(HookCameraModelWaterMark)
    }

    private object HookCameraModelWaterMark : YukiBaseHooker() {
        override fun onHook() {
            val waterMark = prefs(ModulePrefs).getString("custom_model_watermark", "None")
            if (waterMark.isBlank() || waterMark == "None") return

            //Source MarketUtil
            DexkitUtils.searchDexClass(
                "CustomModelWaterMark MarketUtil", appInfo.sourceDir
            ) { dexKitBridge ->
                dexKitBridge.findClass {
                    matcher {
                        fields {
                            addForType(StringClass.name)
                            count(1)
                        }
                        methods {
                            add {
                                paramCount(0)
                                returnType(StringClass.name)
                            }
                            count(2..4)
                        }
                        usingStrings("ro.vendor.oplus.market.enname", "ro.vendor.oplus.market.name")
                    }
                }
            }?.firstOrNull()?.className?.toClass()?.apply {
                method { emptyParam();returnType = StringClass }.giveAll().forEach {
                    it.hook {
                        after {
                            val res = result<String>() ?: return@after
                            if (res.contains("getVendorMarketName")) return@after
                            result = waterMark
                        }
                    }
                }
            }

            //Source WatermarkHelper
            DexkitUtils.searchDexClass(
                "CustomModelWaterMark WatermarkHelper", appInfo.sourceDir
            ) { dexKitBridge ->
                dexKitBridge.findClass {
                    matcher {
                        methods {
                            add { returnType(PaintClass.name) }
                            add { returnType(BitmapClass.name) }
                            add { returnType(UnitType.name) }
                            add { returnType(IntType.name) }
                            add { returnType(FloatType.name) }
                            add { returnType(BooleanType.name) }
                            add { returnType(StringClass.name) }
                            add { paramCount(8) }
                            add { paramTypes(StringClass.name) }
                            add { paramTypes(ContextClass.name, FloatType.name) }
                            add { paramTypes(ContextClass.name, IntType.name, FloatType.name) }
                        }
                        usingStrings("WatermarkHelper", "removeChineseOfString")
                    }
                }
            }?.firstOrNull()?.className?.toClass()?.apply {
                method { param(StringClass);returnType = StringClass }.giveAll().forEach {
                    it.hook {
                        after {
                            val res = result<String>() ?: return@after
                            if (res.contains("removeChineseOfString")) return@after
                            if (res.toIntOrNull() != null) return@after
                            result = waterMark
                        }
                    }
                }
            }
        }
    }
}