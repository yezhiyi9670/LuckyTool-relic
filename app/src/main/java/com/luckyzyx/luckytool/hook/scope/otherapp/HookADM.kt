package com.luckyzyx.luckytool.hook.scope.otherapp

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.SharedPreferences
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ActivityClass
import com.highcapable.yukihookapi.hook.type.android.HandlerClass
import com.highcapable.yukihookapi.hook.type.android.MediaPlayerClass
import com.highcapable.yukihookapi.hook.type.android.PendingIntentClass
import com.highcapable.yukihookapi.hook.type.android.SharedPreferencesClass
import com.highcapable.yukihookapi.hook.type.android.TypefaceClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.HashMapClass
import com.highcapable.yukihookapi.hook.type.java.StringArrayClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookADM : YukiBaseHooker() {
    override fun onHook() {
        val isPro = prefs(ModulePrefs).getBoolean("adm_unlock_pro", false)
        if (!isPro) return
        //Search menu_buy -> firebase.test.lab
        searchClass {
            from("com.dv.get").absolute()
            field { type = SharedPreferencesClass }.count(1)
            field { type = SharedPreferences.Editor::class.java }.count(1)
            field { type = HandlerClass }.count(1)
            field { type = StringArrayClass }.count(5)
            field { type = HashMapClass }.count(3)
            field { type = TypefaceClass }.count(2)
            field { type = ClipboardManager::class.java }.count(1)
            field { type = AlarmManager::class.java }.count(2)
            field { type = NotificationManager::class.java }.count(1)
            field { type = MediaPlayerClass }.count(1)
            field { type = PendingIntentClass }.count(4)
        }.get()?.hook {
            injectMember {
                method {
                    modifiers { isStatic }
                    param(ActivityClass)
                    returnType = UnitType
                }.all()
                afterHook {
                    field {
                        name = "n"
                        modifiers { isStatic }
                        type(BooleanType)
                    }.get().setTrue()
                }
            }
        }
    }
}