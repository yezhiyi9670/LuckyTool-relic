package com.luckyzyx.luckytool.hook.scope.camera

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ActivityClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.SizeClass
import com.highcapable.yukihookapi.hook.type.android.TypefaceClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomModelWaterMark : YukiBaseHooker() {
    override fun onHook() {
        val waterMark = prefs(ModulePrefs).getString("custom_model_watermark", "None")
        if (waterMark.isBlank() || waterMark == "None") return

        //Source SloganUtil
        searchClass {
            from("com.oplus.camera").absolute()
            field().count(2)
            field { type = BooleanType }.count(1)
            method { param { it[0] == ActivityClass } }.count(2)
        }.onNoClassDefFoundError {
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
            } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark")
        }

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
        } ?: loggerD(msg = "$packageName\nError -> CustomModelWaterMark")
    }
}