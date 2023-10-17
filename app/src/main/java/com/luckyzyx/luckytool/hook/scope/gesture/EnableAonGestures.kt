package com.luckyzyx.luckytool.hook.scope.gesture

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.checkDataList

object EnableAonGestures : YukiBaseHooker() {
    override fun onHook() {
        //Search oplus.software.aon_enable
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
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
            }.apply {
                checkDataList("EnableAonGestures findClass")
                val clazz = first()
                //oplus.software.aon_enable
                dexKitBridge.findMethod {
                    searchPackages(clazz.name)
                    matcher {
                        paramTypes(ContextClass.name)
                        returnType(BooleanType.name)
                        usingStrings("oplus.software.aon_enable")
                    }
                }.apply {
                    checkDataList("EnableAonGestures find aon_enable")
                    val member = first()
                    first().apply {
                        member.className.toClass().method {
                            name = member.methodName;param(ContextClass);returnType(BooleanType)
                        }.hook().replaceToTrue()
                    }
                }
                //oplus.software.aon_gestureui_enable
                dexKitBridge.findMethod {
                    searchPackages(clazz.name)
                    matcher {
                        paramTypes(ContextClass.name)
                        returnType(BooleanType.name)
                        usingStrings("oplus.software.aon_gestureui_enable")
                    }
                }.apply {
                    checkDataList("EnableAonGestures find aon_enable")
                    val member = first()
                    member.className.toClass().method {
                        name = member.methodName;param(ContextClass);returnType(BooleanType)
                    }.hook().replaceToTrue()
                }
            }
        }
    }
}