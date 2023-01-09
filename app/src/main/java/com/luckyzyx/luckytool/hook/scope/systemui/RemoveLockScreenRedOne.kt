package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.StringClass

object RemoveLockScreenRedOne : YukiBaseHooker() {
    override fun onHook() {
        //Source RedTextClock
        "com.oplusos.systemui.keyguard.clock.RedTextClock".toClass().field {
            name = "NUMBER_ONE"
            type = StringClass
        }.get().set("")
    }
}