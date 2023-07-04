package com.luckyzyx.luckytool.hook.scope.battery

import android.os.Handler
import android.os.Looper
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BroadcastReceiverClass
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.android.PowerManagerClass
import com.highcapable.yukihookapi.hook.type.android.SharedPreferencesClass
import com.highcapable.yukihookapi.hook.type.java.IntType

object LauncherHighTempreatureProtection : YukiBaseHooker() {
    override fun onHook() {
        //Source ThermalHandler high_temperature_shutdown_message / high_temperature_dialog_auto
        //Key oplus_settings_hightemp_protect
        searchClass {
            from(
                "com.oplus.modulehub.hightemp.model",
                "com.oplus.modulehub.c.a",
                "b6", "c6", "w4", "q4", "z5"
            ).absolute()
            field { type = ContextClass }.count(2)
            field { type = IntType }.count(3..5)
            field { type = SharedPreferencesClass }.count(1)
            field { type = PowerManagerClass }.count(1)
            field { type = BroadcastReceiverClass }.count(1)
            field { type = HandlerClass }.count(1)
            method { param(ContextClass) }.count(2)
            method { param(IntType, IntType) }.count(1)
            method { name = "handleMessage" }.count(1)
        }.get()?.hook {
            injectMember {
                constructor { paramCount = 3 }
                afterHook {
                    field { type = HandlerClass }.get(instance)
                        .set(Handler(Looper.getMainLooper()))
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> LauncherHighTempreatureProtection")
    }
}