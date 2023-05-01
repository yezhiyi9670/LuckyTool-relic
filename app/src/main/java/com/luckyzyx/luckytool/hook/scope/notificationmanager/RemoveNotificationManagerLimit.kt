package com.luckyzyx.luckytool.hook.scope.notificationmanager

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveNotificationManagerLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source ControllerChannelGroup$AppItemListener -> 通知渠道列表允许通知锁
        findClass("com.oplus.notificationmanager.property.uicontroller.ControllerChannelGroup\$AppItemListener").hook {
            injectMember {
                method {
                    name = "isSwitchEnabled"
                }
                replaceToTrue()
            }
        }
        //Source ControllerAllowNotificationChannel -> 通知渠道内允许通知锁
        findClass("com.oplus.notificationmanager.property.uicontroller.ControllerAllowNotificationChannel").hook {
            injectMember {
                method {
                    name = "isNormAppEnabled"
                }
                replaceToTrue()
            }
        }
        //Source ControllerAllowNotificationPkg -> 应用内允许通知锁
        findClass("com.oplus.notificationmanager.property.uicontroller.ControllerAllowNotificationPkg").hook {
            injectMember {
                method {
                    name = "isNormAppEnabled"
                }
                replaceToTrue()
            }
        }
    }
}