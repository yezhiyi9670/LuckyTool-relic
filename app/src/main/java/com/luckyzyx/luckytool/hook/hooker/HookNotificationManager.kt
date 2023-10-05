package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.notificationmanager.RemoveNotificationManagerLimit
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookNotificationManager : YukiBaseHooker() {
    override fun onHook() {
        //移除通知管理限制
        if (prefs(ModulePrefs).getBoolean("remove_notification_manager_limit", false)) {
            loadHooker(RemoveNotificationManagerLimit)
        }

        //通知智能隐藏
        //com.oplus.notificationmanager.SmartAntiVoyeurActivity
        //com.oplus.notificationmanager.fragments.antivoyeur.SmartAntiVoyeurFragment
//            findClass("com.oplus.notificationmanager.config.FeatureOption").hook {
//                injectMember {
//                    method { name = "loadFeatureOption" }
//                    afterHook {
//                        field { name = "IS_AON_ANT_PEEP_DISABLE" }.get().setFalse()
//                    }
//                }
//                injectMember {
//                    method { name = "loadFeatureServiceOption" }
//                    afterHook {
//                        field { name = "IS_AON_ANT_PEEP_DISABLE" }.get().setFalse()
//                    }
//                }
//                injectMember {
//                    method { name = "isSmartAntiVoyeurEnabled" }
//                    replaceToTrue()
//                }
//            }
    }
}