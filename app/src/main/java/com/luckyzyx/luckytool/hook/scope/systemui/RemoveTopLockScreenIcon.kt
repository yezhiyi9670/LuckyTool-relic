package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object RemoveTopLockScreenIcon : YukiBaseHooker() {
    override fun onHook() {
        //Source LockIcon
        "com.android.systemui.statusbar.phone.LockIcon".toClass().apply {
            method { name = "updateIconVisibility" }.hook {
                before { args(0).setFalse() }
            }
        }
    }
}