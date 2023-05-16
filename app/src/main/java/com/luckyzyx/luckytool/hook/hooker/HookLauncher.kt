package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.launcher.FolderLayoutRowColume
import com.luckyzyx.luckytool.hook.scope.launcher.HookAppBadge
import com.luckyzyx.luckytool.hook.scope.launcher.LauncherLayoutRowColume
import com.luckyzyx.luckytool.hook.scope.launcher.LongPressAppIconOpenAppDetails
import com.luckyzyx.luckytool.hook.scope.launcher.PageIndicator
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveAppUpdateDot
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveBottomAppIconOfRecentTaskList
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveRecentTaskListClearButton
import com.luckyzyx.luckytool.hook.scope.launcher.StackedTaskLayout
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookLauncher : YukiBaseHooker() {
    override fun onHook() {
        //分页组件
        loadHooker(PageIndicator)
        //堆叠布局
        loadHooker(StackedTaskLayout)
        //应用徽章
        loadHooker(HookAppBadge)
        //移除APP更新圆点
        if (prefs(ModulePrefs).getBoolean("remove_the_dot_after_app_update", false)) {
            loadHooker(RemoveAppUpdateDot)
        }
        //设置桌面布局行列数
        if (prefs(ModulePrefs).getBoolean("launcher_layout_enable", false)) {
            loadHooker(LauncherLayoutRowColume)
        }
        //文件夹布局调节
        if (prefs(ModulePrefs).getBoolean("enable_folder_layout_adjustment", false)) {
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