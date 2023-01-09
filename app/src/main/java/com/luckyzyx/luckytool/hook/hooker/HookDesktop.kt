package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.alarmclock.RemoveAlarmClockWidgetRedOne
import com.luckyzyx.luckytool.hook.scope.launcher.*
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

object HookDesktop : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.coloros.alarmclock") {
            //移除桌面时钟组件红一
            if (prefs(XposedPrefs).getBoolean("remove_alarmclock_widget_redone", false)) {
                loadHooker(RemoveAlarmClockWidgetRedOne)
            }
        }
        loadApp("com.android.launcher") {
            //移除APP更新圆点
            if (prefs(XposedPrefs).getBoolean("remove_appicon_dot", false)) {
                if (SDK >= A13) loadHooker(RemoveAppUpdateDotV13) else loadHooker(RemoveAppUpdateDot)
            }
            //设置桌面布局行和列
            if (prefs(XposedPrefs).getBoolean("launcher_layout_enable", false)) {
                loadHooker(LauncherLayoutRowColume)
            }
            //设置桌面文件夹行列数
            if (prefs(XposedPrefs).getBoolean("set_folder_layout_4x4", false)) {
                loadHooker(FolderLayoutRowColume)
            }
            //移除最近任务列表清除按钮
            if (prefs(XposedPrefs).getBoolean("remove_recent_task_list_clear_button", false)) {
                loadHooker(RemoveRecentTaskListClearButton)
            }
        }
    }
}