package com.luckyzyx.luckytool.hook.scope.camera

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ActivityClass
import com.highcapable.yukihookapi.hook.type.android.BitmapClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.PaintClass
import com.highcapable.yukihookapi.hook.type.android.SizeClass
import com.highcapable.yukihookapi.hook.type.android.TypefaceClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntArrayType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomModelWaterMark : YukiBaseHooker() {
    override fun onHook() {
        val isRealme = Build.MODEL.contains("RM", true)
        if (isRealme) return
        loadHooker(HookCameraModelWaterMark)
    }

    private object HookCameraModelWaterMark : YukiBaseHooker() {
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
                        else result = waterMark
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark MarketUtil")

            //WatermarkHelper
            searchClass {
                from("com.oplus.camera.feature.watermark").absolute()
                field { type = IntArrayType }.count(1)
                method { returnType = UnitType }.count(6)
                method { returnType = PaintClass }.count(1)
                method { returnType = SizeClass }.count(3)
                method { returnType = BitmapClass }.count(2)
                method { emptyParam();returnType = BooleanType }.count(4)
                method { emptyParam();returnType = StringClass }.count(5)
                method { param { it[0] == ContextClass } }.count(14)
                method { param { it[0] == StringClass } }.count(9)
                method { param { it[0] == SizeClass } }.count(4)
                method { param(ContextClass, FloatType);returnType = FloatType }.count(3)
                method { param(ContextClass, IntType, FloatType);returnType = FloatType }.count(1)
                method {
                    param(ContextClass, FloatType, BooleanType, IntType);returnType = FloatType
                }.count(1)
            }.get()?.hook {
                injectMember {
                    method { param(StringClass);returnType = StringClass }.all()
                    afterHook {
                        val res = result<String>() ?: return@afterHook
                        if (res.contains("removeChineseOfString")) return@afterHook
                        result = waterMark
                    }
                }
            } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark WatermarkHelper")

            //Source SloganUtil
            searchClass {
                from("com.oplus.camera").absolute()
                field().count(2)
                field { type = BooleanType }.count(1)
                method { param { it[0] == ActivityClass } }.count(2)
            }.ignored().onNoClassDefFoundError {
                searchClass {
                    from("com.oplus.camera").absolute()
                    field { type = ActivityClass }.count(1)
                    field { type = SizeClass }.count(1)
                    field { type = IntType }.count(6)
                    field { type = BooleanType }.count(1)
                    field { type = StringClass }.count(2)
                    field { type = TypefaceClass }.count(3)
                    method { param { it[0] == ActivityClass } }.count(2)
                    method { param { it[0] == ContextClass } }.count(6)
                    method { emptyParam();returnType = BooleanType }.count(1)
                    method { emptyParam();returnType = StringClass }.count(1)
                    method { param(IntType);returnType = StringClass }.count(1)
                }.get()?.hook {
                    injectMember {
                        method { emptyParam();returnType = StringClass }
                        afterHook {
                            val res = result<String>() ?: return@afterHook
                            if (res.contains("getVendorMarketName")) return@afterHook
                            else result = waterMark
                        }
                    }
                } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark SloganUtil")
            }
        }
    }
}