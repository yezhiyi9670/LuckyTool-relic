package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.phonemanager.RemoveVirusRiskNotificationInPhoneManager
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookPhoneManager : YukiBaseHooker() {
    override fun onHook() {
        //移除手机管家发现病毒风险通知
        if (prefs(ModulePrefs).getBoolean(
                "remove_virus_risk_notification_in_phone_manager", false
            )
        ) {
            loadHooker(RemoveVirusRiskNotificationInPhoneManager)
        }
    }
}