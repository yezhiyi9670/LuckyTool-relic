package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHiddenEntrance

object HookBattery : YukiBaseHooker() {
    override fun onHook() {
        //屏幕省电,电池健康
        loadHooker(BatteryHiddenEntrance)
    }
}