package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.battery.UnlockStartupLimitV13
import com.luckyzyx.luckytool.hook.apps.launcher.UnlockTaskLocks
import com.luckyzyx.luckytool.hook.apps.launcher.UnlockTaskLocksV11
import com.luckyzyx.luckytool.hook.apps.safecenter.UnlockStartupLimit
import com.luckyzyx.luckytool.hook.apps.safecenter.UnlockStartupLimitV11
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.XposedPrefs

class HookApplication : YukiBaseHooker() {
    override fun onHook() {

        //移除自启数量限制
        if (prefs(XposedPrefs).getBoolean("unlock_startup_limit",false)) {
            if (SDK >= A13){
                //电池
                loadApp("com.oplus.battery",UnlockStartupLimitV13())
            }else{
                //安全中心
                loadApp("com.oplus.safecenter",UnlockStartupLimit())
                //Android11
                loadApp("com.coloros.safecenter", UnlockStartupLimitV11())
            }
        }

        //解锁后台任务限制
        if (prefs(XposedPrefs).getBoolean("unlock_task_locks",false)) {
            //系统桌面
            loadApp("com.android.launcher",UnlockTaskLocks())
            //Android11
            loadApp("com.oppo.launcher", UnlockTaskLocksV11())
        }
    }
}