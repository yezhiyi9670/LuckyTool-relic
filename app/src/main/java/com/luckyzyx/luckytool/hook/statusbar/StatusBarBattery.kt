package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarBatteryInfoNotify
import com.luckyzyx.luckytool.hook.scope.systemui.StatusBarPower
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.SDK

object StatusBarBattery : YukiBaseHooker() {
    override fun onHook() {
        //电池图标
        loadHooker(StatusBarPower)

        //电池信息通知
        if (SDK >= A12) loadHooker(StatusBarBatteryInfoNotify)
    }
}