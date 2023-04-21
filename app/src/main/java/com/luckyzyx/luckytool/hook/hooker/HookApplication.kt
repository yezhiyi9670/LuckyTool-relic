package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.UnlockStartupLimitV13
import com.luckyzyx.luckytool.hook.scope.launcher.UnlockTaskLocks
import com.luckyzyx.luckytool.hook.scope.launcher.UnlockTaskLocksV11
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimit
import com.luckyzyx.luckytool.hook.scope.safecenter.UnlockStartupLimitV11
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookApplication : YukiBaseHooker() {
    override fun onHook() {
        //移除自启数量限制
        if (prefs(ModulePrefs).getBoolean("unlock_startup_limit", false)) {
            if (SDK >= A13) {
                //电池
                if (packageName == "com.oplus.battery") loadHooker(UnlockStartupLimitV13)
            } else {
                //安全中心
                if (packageName == "com.oplus.safecenter") loadHooker(UnlockStartupLimit)
                //Android11
                if (packageName == "com.coloros.safecenter") loadHooker(UnlockStartupLimitV11)
            }
        }

        //解锁后台任务限制
        if (prefs(ModulePrefs).getBoolean("unlock_task_locks", false)) {
            //系统桌面
            if (packageName == "com.android.launcher") loadHooker(UnlockTaskLocks)
            //Android11
            if (packageName == "com.oppo.launcher") loadHooker(UnlockTaskLocksV11)
        }
    }
}