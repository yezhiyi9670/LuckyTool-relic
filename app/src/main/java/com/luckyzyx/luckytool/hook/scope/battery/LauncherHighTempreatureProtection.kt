package com.luckyzyx.luckytool.hook.scope.battery

import android.os.Handler
import android.os.Looper
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BroadcastReceiverClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.android.PowerManagerClass
import com.highcapable.yukihookapi.hook.type.android.SharedPreferencesClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.DexkitUtils

object LauncherHighTempreatureProtection : YukiBaseHooker() {
    const val key = "LauncherHighTempreatureProtection"

    override fun onHook() {
        //Source ThermalHandler high_temperature_shutdown_message / high_temperature_dialog_auto
        //Key oplus_settings_hightemp_protect
        DexkitUtils.searchDexClass(
            "LauncherHighTempreatureProtection", appInfo.sourceDir
        ) { dexKitBridge ->
            dexKitBridge.findClass {
                matcher {
                    fields {
                        addForType(IntType.name)
                        addForType(ContextClass.name)
                        addForType(HandlerClass.name)
                        addForType(PowerManagerClass.name)
                        addForType(SharedPreferencesClass.name)
                        addForType(BroadcastReceiverClass.name)
                    }
                    methods {
                        add("handleMessage")
                        add { paramTypes(ContextClass.name) }
                        add { paramTypes(IntType.name, IntType.name) }
                    }
                }
            }
        }?.firstOrNull()?.className?.hook {
            injectMember {
                constructor { paramCount = 3 }
                afterHook {
                    field { type = HandlerClass }.get(instance)
                        .set(Handler(Looper.getMainLooper()))
                }
            }
        }
    }
}