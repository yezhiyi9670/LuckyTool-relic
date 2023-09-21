package com.luckyzyx.luckytool.hook.scope.screenshot

import android.graphics.Bitmap
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.DexkitUtils

object EnablePNGSaveFormat : YukiBaseHooker() {

    override fun onHook() {
        //Source ImageFileFormat -> JPEG / PNG
        DexkitUtils.searchDexClass("EnablePNGSaveFormat", appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(StringClass.name)
                        addForType(Bitmap.CompressFormat::class.java.name)
                    }
                    methods {
                        add("values")
                        add { returnType(StringClass.name) }
                        add { returnType(Bitmap.CompressFormat::class.java.name) }
                    }
                    usingStrings("image/jpeg", "image/png")
                }
            }
        }?.firstOrNull()?.className?.hook {
            injectMember {
                method { returnType = StringClass }.all()
                afterHook {
                    result = when (result<String>()) {
                        "image/jpeg" -> "image/png"
                        ".jpg" -> ".png"
                        else -> return@afterHook
                    }
                }
            }
            injectMember {
                method { returnType = Bitmap.CompressFormat::class.java }
                afterHook {
                    result = when (result<Bitmap.CompressFormat>()) {
                        Bitmap.CompressFormat.JPEG -> Bitmap.CompressFormat.PNG
                        else -> return@afterHook
                    }
                }
            }
        }
    }
}