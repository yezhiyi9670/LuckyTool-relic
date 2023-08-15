package com.luckyzyx.luckytool.hook.scope.gesture

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object EnableAonGestures : YukiBaseHooker() {
    override fun onHook() {
        //Search oplus.software.aon_enable
        searchClass {
            from("p6", "r6", "r5", "z5").absolute()
            field { type = BooleanType }.count(11..13)
            method { emptyParam();returnType = BooleanType }.count(1..2)
            method { param(ContextClass);returnType = BooleanType }.count(8)
        }.get()?.hook {
            //oplus.software.aon_gestureui_enable
            injectMember {
                method {
                    name = "c"
                    param(ContextClass)
                    returnType = BooleanType
                }
                replaceToTrue()
            }
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
            injectMember {
                method {
                    name = "g"
                    param(ContextClass)
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        } ?: loggerD(msg = "$packageName\nError -> EnableAonGestures")
    }
}