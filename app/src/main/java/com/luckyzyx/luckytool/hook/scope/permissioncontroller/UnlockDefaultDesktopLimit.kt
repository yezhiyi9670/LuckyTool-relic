package com.luckyzyx.luckytool.hook.scope.permissioncontroller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.DexkitUtils
import com.luckyzyx.luckytool.utils.DexkitUtils.printLog
import org.luckypray.dexkit.query.enums.UsingType

object UnlockDefaultDesktopLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source FeatureOption -> oplus.software.defaultapp.remove_force_launcher
        DexkitUtils.create(appInfo.sourceDir) { dexKitBridge ->
            val forceMethod = dexKitBridge.findMethod {
                matcher {
                    addUsingField {
                        matcher {
                            addPutMethod {
                                paramTypes(ContextClass.name)
                                returnType(UnitType.name)
                                usingStrings(
                                    "oplus.software.pms_app_frozen",
                                    "oplus.software.defaultapp.remove_force_launcher",
                                    "oplus.hardware.type.tablet"
                                )
                            }
                            addGetMethod {
                                paramCount(0)
                                returnType(BooleanType.name)
                            }
                        }
                        usingType(UsingType.Get)
                    }
                    paramCount(0)
                    returnType(BooleanType.name)
                    addCall {
                        declaredClass {
                            usingStrings("DefaultApp")
                        }
                        paramTypes("java.util.List")
                        returnType(UnitType.name)
                        usingStrings("DefaultApp")
                    }
                }
            }.apply {
                if (size != 2) this.printLog("UnlockDefaultDesktopLimit allMethod")
            }
            val tableMethod = dexKitBridge.findMethod {
                searchPackages(forceMethod.first().className)
                matcher {
                    addUsingField {
                        matcher {
                            addPutMethod {
                                paramTypes(ContextClass.name)
                                returnType(UnitType.name)
                                usingStrings(
                                    "oplus.software.pms_app_frozen",
                                    "oplus.software.defaultapp.remove_force_launcher",
                                    "oplus.hardware.type.tablet"
                                )
                            }
                            addGetMethod {
                                paramCount(0)
                                returnType(BooleanType.name)
                            }
                        }
                        usingType(UsingType.Get)
                    }
                    paramCount(0)
                    returnType(BooleanType.name)
                    addCall {
                        declaredClass {
                            usingStrings("DefaultApp")
                        }
                        paramTypes("java.util.List")
                        returnType(UnitType.name)
                        usingStrings("DefaultApp")
                    }
                    addCall {
                        declaredClass {
                            usingStrings("DefaultApp")
                        }
                        paramCount(6)
                        returnType(UnitType.name)
                        usingStrings("DefaultApp")
                    }
                }
            }.printLog("UnlockDefaultDesktopLimit tableMethod")
            forceMethod.remove(tableMethod?.firstOrNull())
            forceMethod.apply {
                printLog("UnlockDefaultDesktopLimit finalMethod")
                if (size == 1) firstOrNull()?.apply {
                    className.toClass().apply {
                        method { name = methodName }.hook {
                            replaceToTrue()
                        }
                    }
                }
            }
        }

//        DexkitUtils.searchDexClass("UnlockDefaultDesktopLimit", appInfo.sourceDir) { dexKitBridge ->
//            dexKitBridge.findClass {
//                matcher {
//                    fields {
//                        addForType(BooleanType.name)
//                    }
//                    methods {
//                        add { paramCount(1);returnType(UnitType.name) }
//                        add { paramCount(0);returnType(BooleanType.name) }
//                        add { paramTypes(ContextClass.name);returnType(UnitType.name) }
//                    }
//                    usingStrings(
//                        "oplus.software.pms_app_frozen",
//                        "oplus.software.defaultapp.remove_force_launcher",
//                        "oplus.hardware.type.tablet",
//                        "persist.sys.permission.enable"
//                    )
//                }
//            }
//        }?.firstOrNull()?.className?.toClass()?.apply {
//            method { param(ContextClass) }.hook {
//                after { field { type(BooleanType).index(1) }.get().setTrue() }
//            }
//        }
    }
}