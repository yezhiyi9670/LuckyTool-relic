package com.luckyzyx.luckytool.hook.scope.pictorial

import android.graphics.Bitmap
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BitmapClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.FileClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass

object RemoveImageSaveWaterMark : YukiBaseHooker() {
    override fun onHook() {
        //Search ImageSaveManager -> getWaterMaskBitmap -> standard_water_mask_template / high_quality_water_mask_template
        searchClass {
            from(
                "com.heytap.pictorial.feature.slide.ui.saveconfig",
                "hi", "fj", "ij", "ej", "cj", "mi", "pi", "ri", "wi"
            ).absolute()
            field { type = HandlerClass }.count(1)
            field { type = FileClass }.count(1)
            field { type = LongType }.count(1)
            field { type = BooleanType }.count(2)
            field { type = StringClass }.count(3)
            method { param(ContextClass) }.count(1)
            method { emptyParam();returnType = HandlerClass }.count(1)
            method { returnType = BitmapClass }.count(5)
            method {
                param(BooleanType, VagueType, BitmapClass, BooleanType)
                returnType = BitmapClass
            }.count(1)
            method {
                param("com.heytap.pictorial.core.bean.BasePictorialData".toClass())
                returnType = BooleanType
            }.count(2)
            method {
                param(StringClass, BitmapClass, BitmapClass, StringClass, BooleanType)
                returnType = BitmapClass
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    param(BooleanType, VagueType, BitmapClass, BooleanType)
                    returnType = BitmapClass
                }
                afterHook { result = args(2).cast<Bitmap>() ?: return@afterHook }
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveImageSaveWaterMark")
    }
}