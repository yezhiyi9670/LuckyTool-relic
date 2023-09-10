package com.luckyzyx.luckytool.hook.scope.camera

import android.text.Spanned
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.android.LayoutInflaterClass
import com.highcapable.yukihookapi.hook.type.android.ViewGroupClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getAppSet

object RemoveWatermarkWordLimit : YukiBaseHooker() {
    override fun onHook() {
        val isNew = "com.oplus.camera.setting.CameraSettingActivity".hasClass()
        if (isNew) loadHooker(HookWordLimit) else loadHooker(HookWordLimitOld)
    }

    private object HookWordLimit : YukiBaseHooker() {
        override fun onHook() {
            val appSet = getAppSet(ModulePrefs, packageName)
            val clazz = when (appSet[2]) {
                else -> "$5"
            }
            //Source CameraSubSettingFragment -> camera_namelength_outofrange -> filter
            searchClass {
                from("com.oplus.camera.setting").absolute()
                field { type = StringClass }.count(1)
                field { type = IntType }.count(2)
                field { type = LongType }.count(2)
                method { name = "onCreate" }.count(1)
                method { name = "onDestroy" }.count(1)
                method { name = "onDestroyView" }.count(1)
                method { name = "onPause" }.count(1)
                method { name = "onPreferenceChange" }.count(1)
                method { name = "onPreferenceClick" }.count(1)
                method { name = "onResume" }.count(1)
                method { name = "onViewCreated" }.count(1)
                method { param(LayoutInflaterClass, ViewGroupClass, BundleClass) }.count(1..2)
                method {
                    param(
                        CharSequenceClass, IntType, IntType, Spanned::class.java, IntType, IntType
                    )
                    paramCount = 6
                    returnType = CharSequenceClass
                }.count(1)
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

    private object HookWordLimitOld : YukiBaseHooker() {
        override fun onHook() {
            val appSet = getAppSet(ModulePrefs, packageName)
            val clazz = when (appSet[2]) {
                "8d5b992", "38e5b1a", "b696b47", "02aac8a" -> "$7"
                else -> "$1"
            }

            //Source CameraSubSettingFragment -> camera_namelength_outofrange -> filter
            //Source CameraPhotoSloganSettingFragment -> camera_namelength_outofrange -> filter
            searchClass {
                from("com.oplus.camera.ui.menu.setting").absolute()
                field { type = IntType }.count(1..2)
                field { type = LongType }.count(1..2)
                method { name = "onDestroy" }.count(1)
                method { name = "onPause" }.count(1)
                method { name = "onPreferenceChange" }.count(1)
                method { name = "onPreferenceClick" }.count(1)
                method { param(StringClass) }.count(2..3)
                method { param { it[0] == BundleClass } }.count(4)
                method { returnType = IntType }.count(1)
                method { returnType = StringClass }.count(1..5)
                method { returnType = BooleanType }.count(4..7)
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



