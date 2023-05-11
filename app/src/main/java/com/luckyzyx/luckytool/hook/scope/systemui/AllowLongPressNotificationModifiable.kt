package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.hasField

object AllowLongPressNotificationModifiable : YukiBaseHooker() {
    override fun onHook() {
        //Source NotificationSettingsModel
        VariousClass(
            "com.oplusos.systemui.notification.settingspanel.NotificationSettingsModel", //OnePlus C13.0
            "com.oplusos.systemui.notification.settingspanel.controller.NotificationController" //Realme C13.1
        ).hook {
            injectMember {
                method {
                    name = when (instanceClass.simpleName) {
                        "NotificationSettingsModel" -> "resolveMode"
                        "NotificationController" -> "resolveSettingsModel"
                        else -> "resolveMode"
                    }
                    paramCount = 1
                }
                beforeHook {
                    val hasField = instance.javaClass.hasField { name = "isAppModifiable" }
                    if (hasField) field { name = "isAppModifiable" }.get(instance).setTrue()
                    else args(0).any()?.current()?.field { name = "isAppModifiable" }?.setTrue()
                }
            }
        }
    }
}