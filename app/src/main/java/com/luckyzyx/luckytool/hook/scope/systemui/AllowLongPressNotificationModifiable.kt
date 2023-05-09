package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object AllowLongPressNotificationModifiable : YukiBaseHooker() {
    override fun onHook() {
        //Source NotificationSettingsModel
        findClass("com.oplusos.systemui.notification.settingspanel.NotificationSettingsModel").hook {
            injectMember {
                method {
                    name = "resolveMode"
                    paramCount = 1
                }
                beforeHook {
                    field { name = "isAppModifiable" }.get(instance).setTrue()
                }
            }
        }
    }
}