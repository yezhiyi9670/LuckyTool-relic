package com.luckyzyx.luckytool.hook.scope.launcher

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object UnlockTaskLocks : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusLockManager
        VariousClass(
            "com.coloros.quickstep.applock.ColorLockManager",
            "com.oplus.quickstep.applock.OplusLockManager"
        ).hook {
            injectMember {
                constructor { paramCount = 1 }
                afterHook {
                    field { name = "mLockAppLimit" }.get(instance).set(999)
                }
            }
        }
    }
}