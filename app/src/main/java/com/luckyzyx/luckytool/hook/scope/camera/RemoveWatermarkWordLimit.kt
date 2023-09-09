package com.luckyzyx.luckytool.hook.scope.camera

import android.view.inputmethod.InputMethodManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.android.LayoutInflaterClass
import com.highcapable.yukihookapi.hook.type.android.ParcelableClass
import com.highcapable.yukihookapi.hook.type.android.ViewGroupClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.getAppSet

object RemoveWatermarkWordLimit : YukiBaseHooker() {
    override fun onHook() {
        if (SDK >= A13) loadHooker(HookWordLimit)
        else loadHooker(HookWordLimitC12)
    }

    private object HookWordLimit : YukiBaseHooker() {
        override fun onHook() {
            val appSet = getAppSet(ModulePrefs, packageName)
            val clazz = when (appSet[2]) {
                "8d5b992", "38e5b1a", "b696b47", "02aac8a" -> "$7"
                else -> "$5"
            }
            // Source CameraSubSettingFragment
            // Log camera_namelength_outofrange -> Method
            searchClass {
                from("com.oplus.camera.setting", "com.oplus.camera.ui.menu.setting").absolute()
                method { name = "onCreate" }.count(0..1)
                method { name = "onDestroy" }.count(1)
                method { name = "onDestroyView" }.count(1)
                method { name = "onPause" }.count(1)
                method { name = "onPreferenceChange" }.count(1)
                method { name = "onPreferenceClick" }.count(1)
                method { name = "onResume" }.count(1)
                method { name = "onViewCreated" }.count(1)
                method { param(BundleClass) }.count(3..4)
                method { param(LayoutInflaterClass, ViewGroupClass, BundleClass) }.count(1..2)
                method {
                    param { it[0] == CharSequenceClass && it[1] == IntType && it[2] == IntType }
                    paramCount = 6
                    returnType = CharSequenceClass
                }.count(0..1)
            }.get()?.apply {
                findClass(canonicalName!! + clazz).hook {
                    injectMember {
                        method { name = "filter";returnType = CharSequenceClass }
                        intercept()
                    }
                }.onHookClassNotFoundFailure {
                    loggerD(msg = "Error -> RemoveWatermarkWordLimit")
                }
            } ?: loggerD(msg = "$packageName\nError -> RemoveWatermarkWordLimit")
        }
    }

    private object HookWordLimitC12 : YukiBaseHooker() {
        override fun onHook() {
            val appSet = getAppSet(ModulePrefs, packageName)
            val clazz = when (appSet[2]) {
                else -> "$1"
            }
            // Source CameraSubSettingFragment
            // Log camera_namelength_outofrange -> Method
            searchClass {
                from("com.oplus.camera.ui.menu.setting").absolute()
                field { type = IntType }.count(1)
                field { type = LongType }.count(1)
                field { type = BooleanType }.count(1)
                field { type = InputMethodManager::class.java }.count(1)
                method { name = "onDestroy" }.count(1)
                method { name = "onPause" }.count(1)
                method { name = "onPreferenceChange" }.count(1)
                method { name = "onPreferenceClick" }.count(1)
                method { param(ParcelableClass) }.count(1)
                method { param { it[0] == BundleClass } }.count(4)
                method { returnType = StringClass }.count(3)
                method { returnType = BooleanType }.count(7)
            }.get()?.apply {
                findClass(canonicalName!! + clazz).hook {
                    injectMember {
                        method { name = "filter";returnType = CharSequenceClass }
                        intercept()
                    }
                }.onHookClassNotFoundFailure {
                    loggerD(msg = "Error -> RemoveWatermarkWordLimit")
                }
            } ?: loggerD(msg = "$packageName\nError -> RemoveWatermarkWordLimit")
        }
    }
}



