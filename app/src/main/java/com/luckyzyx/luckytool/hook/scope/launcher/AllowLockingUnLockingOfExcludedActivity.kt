package com.luckyzyx.luckytool.hook.scope.launcher

import android.content.Intent
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object AllowLockingUnLockingOfExcludedActivity : YukiBaseHooker() {
    override fun onHook() {
        //Search OplusTaskShortcutsFactory -> showLock / showUnlock
        //Source OplusLockManager -> Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS (8388608 / 0x00800000)
        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        findClass("com.oplus.quickstep.applock.OplusLockManager").hook {
            injectMember {
                method { name = "isExcludedFromRecents" }
                replaceToFalse()
            }
        }
    }
}