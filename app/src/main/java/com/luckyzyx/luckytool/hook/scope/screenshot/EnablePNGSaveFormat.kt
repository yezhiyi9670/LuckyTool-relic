package com.luckyzyx.luckytool.hook.scope.screenshot

import android.graphics.Bitmap
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
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
        }?.firstOrNull()?.className?.toClass()?.apply {
            method { returnType = StringClass }.hookAll {
                after {
                    result = when (result<String>()) {
                        "image/jpeg" -> "image/png"
                        ".jpg" -> ".png"
                        else -> return@after
                    }
                }
            }

            method { returnType = Bitmap.CompressFormat::class.java }.hook {
                after {
                    result = when (result<Bitmap.CompressFormat>()) {
                        Bitmap.CompressFormat.JPEG -> Bitmap.CompressFormat.PNG
                        else -> return@after
                    }
                }
            }
        }
    }
}