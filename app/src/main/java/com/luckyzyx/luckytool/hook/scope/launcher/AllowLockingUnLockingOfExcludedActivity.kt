package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method

object AllowLockingUnLockingOfExcludedActivity : YukiBaseHooker() {
    override fun onHook() {
        //Search OplusTaskShortcutsFactory -> showLock / showUnlock
        //Source OplusLockManager -> Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS (8388608 / 0x00800000)
        "com.oplus.quickstep.applock.OplusLockManager".toClass().apply {
            method { name = "isExcludedFromRecents" }.hook {
                replaceToFalse()
            }
        }
    }
}