package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.StringClass

object EnableWatermarkEditing : YukiBaseHooker() {
    override fun onHook() {
        //Source OtherSystemStorage
        searchClass {
            from("oo", "jo", "qr", "qn", "xn", "ho", "uq", "or", "ls").absolute()
            field { type = ContextClass }.count(1)
            method { returnType = IntClass }.count(1)
            method { returnType = LongClass }.count(1)
            method { returnType = BooleanClass }.count(1)
            method { returnType = StringClass }.count(1..2)
            method { returnType = BooleanType }.count(5..7)
        }.get()?.hook {
            injectMember {
                method {
                    param(VagueType, BooleanType)
                    returnType = BooleanClass
                }
                afterHook {
                    val configNode = args().first().any()?.toString() ?: return@afterHook
                    when {
                        //com.oplus.camera.support.custom.hasselblad.watermark
                        configNode.contains("feature_is_support_watermark") -> resultTrue()
                        configNode.contains("feature_is_support_hassel_watermark") -> resultTrue()
                        //is_realme_brand / debug.gallery.photo.editor.watermark.switcher
                        configNode.contains("feature_is_support_photo_editor_watermark") -> resultTrue()
                        //is_realme_brand / debug.gallery.photo.editor.watermark.switcher
                        configNode.contains("feature_is_support_privacy_watermark") -> resultTrue()
                    }
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> EnableHasselbladWatermarkEditing")
    }
}