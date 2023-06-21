package com.luckyzyx.luckytool.hook.scope.screenshot

import android.graphics.Bitmap
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.StringClass

object EnablePNGSaveFormat : YukiBaseHooker() {

    override fun onHook() {
        //Source ImageFileFormat -> JPEG / PNG
        searchClass {
            from(
                "com.oplus.screenshot.save.info",
                "uc", "rc", "tc", "ac", "fa", "ea", "u9"
            ).absolute()
            constructor().count(1)
            field { type = StringClass }.count(2)
            field { type = Bitmap.CompressFormat::class.java }.count(1)
            method { emptyParam();name = "values" }.count(1)
            method { emptyParam();returnType = StringClass }.count(2)
            method { returnType = Bitmap.CompressFormat::class.java }.count(1)
        }.get()?.hook {
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
        } ?: loggerD(msg = "$packageName\nError -> EnablePNGSaveFormat")
    }
}