package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.hasField

object AllowLongPressNotificationModifiable : YukiBaseHooker() {
    override fun onHook() {
        //Source NotificationSettingsModel
        VariousClass(
            "com.oplusos.systemui.notification.settingspanel.NotificationSettingsModel", //C13
            "com.oplusos.systemui.notification.settingspanel.controller.NotificationController", //C13.1
            "com.oplus.systemui.statusbar.notification.settingspanel.controller.NotificationController" //C14
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
                    else args().first().any()?.current()?.field { name = "isAppModifiable" }?.setTrue()
                }
            }
        }
    }
}