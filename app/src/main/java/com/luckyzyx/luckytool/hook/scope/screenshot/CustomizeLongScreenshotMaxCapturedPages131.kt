package com.luckyzyx.luckytool.hook.scope.screenshot

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ComponentNameClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.FloatType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.luckytool.utils.ModulePrefs

object CustomizeLongScreenshotMaxCapturedPages : YukiBaseHooker() {
    override fun onHook() {
        val pages = prefs(ModulePrefs).getInt("customize_long_screenshot_max_captured_pages", 18)
        //Source Configs -> config_max_pages_rgb565
        searchClass {
            from("k6", "p3", "q5", "f6").absolute()
            constructor().count(1)
            field { type = IntType }.count(3)
            method { param(ContextClass) }.count(3)
            method { emptyParam();returnType = IntType }.count(2)
            method { param(ContextClass);returnType = IntType }.count(2)
            method { returnType = IntType }.count(4)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }.all()
                replaceTo(pages * 2)
            }
            injectMember {
                method {
                    param(ContextClass)
                    returnType = IntType
                }.all()
                replaceTo(2147483647)
            }
        }
    }
}

object CustomizeLongScreenshotMaxCapturedPages131 : YukiBaseHooker() {
    override fun onHook() {
        val pages = prefs(ModulePrefs).getInt("customize_long_screenshot_max_captured_pages", 18)
        //Source ScrollCaptureConfigs -> scroll_configs_max_captured_pages / scroll_configs_max_captured_pixels
        searchClass {
            from("ma", "ja", "la", "ka", "s9").absolute()
            constructor().count(1)
            field { type = ContextClass }.count(1)
            field { type = IntType }.count(16)
            field { type = LongType }.count(1)
            field { type = ComponentNameClass }.count(2)
            method { param(ComponentNameClass) }.count(1)
            method { returnType = ComponentNameClass }.count(2)
            method { returnType = ContextClass }.count(1)
            method { returnType = BooleanType }.count(1)
            method { returnType = LongType }.count(1)
            method { returnType = FloatType }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }.all()
                afterHook {
                    if (result<Int>() == 36) result = pages * 2
                    if (result<Int>() == 78643200) result = 2147483647
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> CustomizeLongScreenshotMaxCapturedPages")
    }
}