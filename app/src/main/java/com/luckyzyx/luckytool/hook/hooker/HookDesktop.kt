package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.alarmclock.RemoveAlarmClockWidgetRedOne
import com.luckyzyx.luckytool.hook.scope.launcher.*
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookDesktop : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.coloros.alarmclock") {
            //移除桌面时钟组件红一
            if (prefs(ModulePrefs).getBoolean("remove_alarmclock_widget_redone", false)) {
                loadHooker(RemoveAlarmClockWidgetRedOne)
            }
        }
        if (packageName == "com.android.launcher") {
            //分页组件
            loadHooker(PageIndicator)
            //堆叠布局
            loadHooker(StackedTaskLayout)
            //移除APP更新圆点
            if (prefs(ModulePrefs).getBoolean("remove_appicon_dot", false)) {
                loadHooker(RemoveAppUpdateDot)
            }
            //设置桌面布局行列数
            if (prefs(ModulePrefs).getBoolean("launcher_layout_enable", false)) {
                loadHooker(LauncherLayoutRowColume)
            }
            //文件夹布局
            if (prefs(ModulePrefs).getBoolean("set_folder_layout_4x4", false)) {
                loadHooker(FolderLayoutRowColume)
            }
            //移除最近任务列表清除按钮
            if (prefs(ModulePrefs).getBoolean("remove_recent_task_list_clear_button", false)) {
                loadHooker(RemoveRecentTaskListClearButton)
            }
            //最近任务列表长按APP图标打开应用详情
            if (prefs(ModulePrefs).getBoolean("long_press_app_icon_open_app_details", false)) {
                loadHooker(LongPressAppIconOpenAppDetails)
            }
            //移除最近任务列表底部APP图标
            if (prefs(ModulePrefs).getBoolean(
                    "remove_bottom_app_icon_of_recent_task_list", false
                )
            ) {
                loadHooker(RemoveBottomAppIconOfRecentTaskList)
            }

            //com.android.launcher3.popup.OplusBaseSystemShortcut
            //OplusAppInfo etc. -> Click

            //AppEdit
            //com.oplus.uxicon.ui.ui.UxEditPanelFragment -> res/layout/edit_panel_layout.xml
            //<string name="no_icon_pack_toast">没有支持替换图标的图标包</string>
        }
    }
}