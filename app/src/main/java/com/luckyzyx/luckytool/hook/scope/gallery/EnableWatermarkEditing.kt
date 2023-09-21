package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils

object EnableWatermarkEditing : YukiBaseHooker() {

    override fun onHook() {
        //Source OtherSystemStorage
        DexkitUtils.searchDexClass(
            "EnableWatermarkEditing", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
                        addForType("kotlin.Lazy")
                    }
                    methods {
                        add { paramCount(2);returnType(IntClass.name) }
                        add { paramCount(2);returnType(LongClass.name) }
                        add { paramCount(2);returnType(BooleanClass.name) }
                        add { paramCount(2);returnType(StringClass.name) }
                        add { paramCount(2);returnType(UnitType.name) }
                        add { paramCount(0);returnType(BooleanType.name) }
                        add { paramCount(4);returnType(BooleanType.name) }
                    }
                }
            }
        }?.firstOrNull()?.className?.hook {
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
        }
    }
}