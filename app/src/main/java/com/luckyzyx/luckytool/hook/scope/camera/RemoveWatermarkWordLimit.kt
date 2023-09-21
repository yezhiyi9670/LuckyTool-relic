package com.luckyzyx.luckytool.hook.scope.camera

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getAppSet

object RemoveWatermarkWordLimit : YukiBaseHooker() {
    override fun onHook() {
        val appSet = getAppSet(ModulePrefs, packageName)
        val isNew = "com.oplus.camera.setting.CameraSettingActivity".hasClass()
        val clazz = if (isNew) "$5"
        else when (appSet[2]) {
            "8d5b992", "38e5b1a", "b696b47", "02aac8a" -> "$7"
            else -> "$1"
        }

        //Source CameraSubSettingFragment -> camera_namelength_outofrange -> filter
        //Source CameraSloganSettingFragment -> camera_namelength_outofrange -> filter
        val clsName = DexkitUtils.searchDexClass(
            "RemoveWatermarkWordLimit", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                searchPackages("com.oplus.camera.setting", "com.oplus.camera.ui.menu.setting")
                matcher {
                    fields {
                        addForType(IntType.name)
                        addForType(LongType.name)
                        addForType(BooleanType.name)
                    }
                    methods {
                        add { name = "onDestroy";returnType(UnitType.name) }
                        add { name = "onPause";returnType(UnitType.name) }
                        add { name = "onPreferenceChange";returnType(BooleanType.name) }
                        add { name = "onPreferenceClick";returnType(BooleanType.name) }
                        add { paramTypes(BundleClass.name);returnType(UnitType.name) }
                        add { paramTypes(BundleClass.name);returnType(BooleanType.name) }
                        add { paramTypes(StringClass.name);returnType(UnitType.name) }
                    }
                    if (isNew) usingStrings("CameraSubSettingFragment")
                    else usingStrings(
                        "CameraSloganSettingFragment",
                        "isSloganEnable",
                        "isVideoSloganEnable"
                    )
                }
            }
        }?.firstOrNull()?.className ?: "null"
        findClass(clsName + clazz).hook {
            injectMember {
                method { name = "filter";returnType = CharSequenceClass }
                intercept()
            }
        }.onHookClassNotFoundFailure {
            loggerD(msg = "Error -> RemoveWatermarkWordLimit")
        }
    }
}



