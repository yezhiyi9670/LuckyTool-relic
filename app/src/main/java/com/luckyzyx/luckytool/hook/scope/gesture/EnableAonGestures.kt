package com.luckyzyx.luckytool.hook.scope.gesture

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.DexkitUtils

object EnableAonGestures : YukiBaseHooker() {
    override fun onHook() {
        //Search oplus.software.aon_enable
        DexkitUtils.searchDexClass("EnableAonGestures", appInfo.sourceDir) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(BooleanType.name)
                    }
                    methods {
                        add { paramCount(0);returnType(BooleanType.name) }
                        add { paramTypes(ContextClass.name);returnType(BooleanType.name) }
                    }
                    usingStrings(
                        "oplus.software.aon_gestureui_enable",
                        "oplus.software.aon_gesture_press",
                        "oplus.software.aon_phone_mute",
                        "oplus.software.aon_phone_enable",
                        "oplus.software.aon_enable"
                    )
                }
            }
        }?.firstOrNull()?.className?.toClass()?.apply {
            //oplus.software.aon_gestureui_enable
            method {
                name = "c"
                param(ContextClass)
                returnType = BooleanType
            }.hook { replaceToTrue() }
            //oplus.software.aon_gesture_press
//            injectMember {
//                method {
//                    name = "d"
//                    param(ContextClass)
//                    returnType = BooleanType
//                }
//                replaceToTrue()
//            }
            //oplus.software.aon_phone_mute
//            injectMember {
//                method {
//                    name = "e"
//                    param(ContextClass)
//                    returnType = BooleanType
//                }
//                replaceToTrue()
//            }
            //oplus.software.aon_phone_enable / oplus.software.aon_phone_camera_gesture_recognition
//            injectMember {
//                method {
//                    name = "f"
//                    param(ContextClass)
//                    returnType = BooleanType
//                }
//                replaceToTrue()
//            }
            //oplus.software.aon_enable
            method {
                name = "g"
                param(ContextClass)
                returnType = BooleanType
            }.hook { replaceToTrue() }
        }
    }
}