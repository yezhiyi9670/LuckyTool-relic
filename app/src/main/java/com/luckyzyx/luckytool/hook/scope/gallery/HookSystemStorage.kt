package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookSystemStorage : YukiBaseHooker() {

    override fun onHook() {
        //启用水印编辑
        val waterMark = prefs(ModulePrefs).getBoolean("enable_watermark_editing", false)

        //Source OtherSystemStorage
        DexkitUtils.searchDexClass("HookSystemStorage", appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(ContextClass.name)
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
                    usingStrings("configNode")
                }
            }
        }.toClass().apply {
            method {
                param(VagueType, BooleanType)
                returnType = BooleanClass
            }.hook {
                after {
                    val configNode = args().first().any()?.toString() ?: return@after
                    when {
                        //com.oplus.camera.support.custom.hasselblad.watermark
                        configNode.contains("feature_is_support_watermark") -> if (waterMark) resultTrue()
                        configNode.contains("feature_is_support_hassel_watermark") -> if (waterMark) resultTrue()
                        //is_realme_brand / debug.gallery.photo.editor.watermark.switcher
                        configNode.contains("feature_is_support_photo_editor_watermark") -> if (waterMark) resultTrue()
                        //is_realme_brand / debug.gallery.photo.editor.watermark.switcher
                        configNode.contains("feature_is_support_privacy_watermark") -> if (waterMark) resultTrue()
                        //debug.gallery.lns / os.graphic.gallery.photoview.lns
                        configNode.contains("feature_is_support_lns") -> {
    //                            loggerD(msg = "configNode -> lns call -> $result")
    //                            resultTrue()
                        }
                    }
                }
            }
        }
    }
}