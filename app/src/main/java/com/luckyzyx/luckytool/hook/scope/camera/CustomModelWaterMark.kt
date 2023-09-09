package com.luckyzyx.luckytool.hook.scope.camera

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BitmapClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.PaintClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomModelWaterMark : YukiBaseHooker() {
    override fun onHook() {
        val isRealme = Build.MODEL.startsWith("RM", true)
        if (isRealme) return
        loadHooker(HookCameraModelWaterMark)
    }

    private object HookCameraModelWaterMark : YukiBaseHooker() {
        @Suppress("unused")
        override fun onHook() {
            val waterMark = prefs(ModulePrefs).getString("custom_model_watermark", "None")
            if (waterMark.isBlank() || waterMark == "None") return

            //Source MarketUtil
            searchClass {
                from("com.oplus.camera.common.utils").absolute()
                field().count(1)
                field { type = StringClass }.count(1)
                constructor().none()
                method().count(2..4)
                method { returnType = StringClass }.count(2..4)
                method { emptyParam();returnType = StringClass }.count(2)
            }.get()?.hook {
                injectMember {
                    method { emptyParam();returnType = StringClass }.all()
                    afterHook {
                        val res = result<String>() ?: return@afterHook
                        if (res.contains("getVendorMarketName")) return@afterHook
                        result = waterMark
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark MarketUtil")

            //Source WatermarkHelper
            searchClass {
                from("com.oplus.camera.feature.watermark").absolute()
                method { returnType = PaintClass }.count(1)
                method { returnType = BitmapClass }.count(2)
                method { returnType = UnitType }.count(3..6)

                method { param { it[0] == ContextClass } }.count(10..14)
                method { param { it[0] == StringClass } }.count(8..9)
                method { paramCount = 8;returnType = StringClass }.count(2)

                method { param(StringClass);returnType = IntType }.count(1)
                method { param(StringClass);returnType = BooleanType }.count(1)
                method { param(StringClass);returnType = StringClass }.count(3..4)

                method { param(ContextClass, FloatType);returnType = FloatType }.count(1..3)
                method { param(ContextClass, IntType, FloatType);returnType = FloatType }.count(1)
            }.get()?.hook {
                injectMember {
                    method { param(StringClass);returnType = StringClass }.all()
                    afterHook {
                        val res = result<String>() ?: return@afterHook
                        if (res.contains("removeChineseOfString")) return@afterHook
                        if (res.toIntOrNull() != null) return@afterHook
                        result = waterMark
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark WatermarkHelper")
        }
    }
}