package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.notificationmanager.RemoveNotificationManagerLimit
import com.luckyzyx.luckytool.hook.scope.systemui.AllowLongPressNotificationModifiable
import com.luckyzyx.luckytool.utils.ModulePrefs

object StatusBarNotifiyLimit : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.android.systemui") {
            //允许长按通知可修改
            if (prefs(ModulePrefs).getBoolean("allow_long_press_notification_modifiable", false)) {
                loadHooker(AllowLongPressNotificationModifiable)
            }
        }
        if (packageName == "com.oplus.notificationmanager") {
            //移除通知管理限制
            if (prefs(ModulePrefs).getBoolean("remove_notification_manager_limit", false)) {
                loadHooker(RemoveNotificationManagerLimit)
            }

            //通知智能隐藏
            //com.oplus.notificationmanager.SmartAntiVoyeurActivity
            //com.oplus.notificationmanager.fragments.antivoyeur.SmartAntiVoyeurFragment
//            findClass("com.oplus.notificationmanager.config.FeatureOption").hook {
//                injectMember {
//                    method { name = "isSmartAntiVoyeurEnabled" }
//                    replaceToTrue()
//                }
//            }
        }
    }
}