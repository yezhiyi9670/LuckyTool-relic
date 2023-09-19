package com.luckyzyx.luckytool.hook.scope.gallery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.BooleanClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntClass
import com.highcapable.yukihookapi.hook.type.java.LongClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitPrefs
import com.luckyzyx.luckytool.utils.ModulePrefs
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

object EnableWatermarkEditing : YukiBaseHooker() {
    const val key = "enable_watermark_editing"

    override fun onHook() {
        val isEnable = prefs(ModulePrefs).getBoolean(key, false)
        if (!isEnable) return

        val clsName = prefs(DexkitPrefs).getString(key, "null")
        //Source OtherSystemStorage
        findClass(clsName).hook {
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

    fun searchDexkit(bridge: DexKitBridge?): ClassDataList? {
        val result = bridge?.findClass {
            searchPackages = listOf("oo", "jo", "qr", "qn", "xn", "ho", "uq", "or", "ls")
            matcher {
                fields {
                    addForType(ContextClass.name)
                    addForType(Lazy::class.java.name)
                }
                methods {
                    add { returnType = IntClass.name }
                    add { returnType = LongClass.name }
                    add { returnType = BooleanClass.name }
                    add { returnType = StringClass.name }
                    add { returnType = BooleanType.name }
                    count(10..20)
                }
            }
        }
        return result
    }
}