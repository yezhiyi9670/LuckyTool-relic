package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.oplus.RemoveHotspotPowerConsumptionNotification

object HookOplus : YukiBaseHooker() {
    override fun onHook() {
        //移除热点耗电通知
        loadHooker(RemoveHotspotPowerConsumptionNotification)

    }
}
