package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.launcher.AllowLockingUnLockingOfExcludedActivity
import com.luckyzyx.luckytool.hook.scope.launcher.FolderLayoutRowColume
import com.luckyzyx.luckytool.hook.scope.launcher.HookAppBadge
import com.luckyzyx.luckytool.hook.scope.launcher.LauncherLayoutRowColume
import com.luckyzyx.luckytool.hook.scope.launcher.LongPressAppIconOpenAppDetails
import com.luckyzyx.luckytool.hook.scope.launcher.PageIndicator
import com.luckyzyx.luckytool.hook.scope.launcher.RecentTaskListClearButton
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveAppUpdateDot
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveBottomAppIconOfRecentTaskList
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveFolderPreviewBackground
import com.luckyzyx.luckytool.hook.scope.launcher.RemoveLauncherHighTempreatureProtection
import com.luckyzyx.luckytool.hook.scope.launcher.StackedTaskLayout
import com.luckyzyx.luckytool.hook.scope.launcher.UnlockTaskLocks
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookLauncher : YukiBaseHooker() {
    override fun onHook() {
        //分页组件
        loadHooker(PageIndicator)
        //堆叠布局
        loadHooker(StackedTaskLayout)
        //应用徽章
        if (SDK >= A13) loadHooker(HookAppBadge)
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
        //移除文件夹预览背景
        if (prefs(ModulePrefs).getBoolean("remove_folder_preview_background", false)) {
            loadHooker(RemoveFolderPreviewBackground)
        }
        //最近任务列表清除按钮
        if (prefs(ModulePrefs).getBoolean("remove_recent_task_list_clear_button", false)) {
            loadHooker(RecentTaskListClearButton)
        }
        //最近任务列表长按APP图标打开应用详情
        if (prefs(ModulePrefs).getBoolean("long_press_app_icon_open_app_details", false)) {
            loadHooker(LongPressAppIconOpenAppDetails)
        }
        //移除最近任务列表底部APP图标
        if (prefs(ModulePrefs).getBoolean("remove_bottom_app_icon_of_recent_task_list", false)) {
            loadHooker(RemoveBottomAppIconOfRecentTaskList)
        }
        //解锁后台任务锁定限制
        if (prefs(ModulePrefs).getBoolean("unlock_task_locks", false)) {
            loadHooker(UnlockTaskLocks)
        }
        //移除桌面高温保护
        if (prefs(ModulePrefs).getBoolean("remove_launcher_high_tempreature_protection", false)) {
            loadHooker(RemoveLauncherHighTempreatureProtection)
        }
        //允许锁定或解锁已排除活动
        if (prefs(ModulePrefs).getBoolean("allow_locking_unlocking_of_excluded_activity", false)) {
            loadHooker(AllowLockingUnLockingOfExcludedActivity)
        }

//        //KEY_APP_UPDATE_DOT / app_update_dot
//        findClass("com.android.launcher.settings.LauncherSettingsUtils").hook {
//            injectMember {
//                method { name = "isSupportAppUpdateDot" }
//                replaceToTrue()
//            }
//        }

//        //Source LauncherSettingsFragment
//        findClass("com.android.launcher.settings.LauncherSettingsFragment").hook {
//            injectMember {
//                method { name = "initPreferences" }
//                afterHook {
//                    val context = field { name = "mContext" }.get(instance).cast<Context>()
//                        ?: return@afterHook
//                    val mAppStartupSpeedPreference =
//                        field { name = "mAppStartupSpeedPreference" }.get(instance).any()
//                            ?: return@afterHook
//                    val arrayId = context.resources.getIdentifier(
//                        "app_startup_anim_speed", "array", packageName
//                    ).takeIf { e -> e != 0 } ?: return@afterHook
//                    val stringArray =
//                        safeOfNull { context.resources.getStringArray(arrayId) } ?: return@afterHook
//                    mAppStartupSpeedPreference.current().method {
//                        name = "setEntries"
//                        param(CharSequenceArrayClass)
//                    }.call(stringArray)
//                }
//            }
//        }
//
//        findClass("com.android.launcher3.anim.AppLaunchAnimSpeedHandler").hook {
//            injectMember {
//                method { name = "isNotSupportSpeedModify" }
//                replaceToFalse()
//            }
//            injectMember {
//                constructor { paramCount = 1 }
//                afterHook {
//                    val launcher = args().first().any() ?: return@afterHook
//                    val contentResolver = launcher.current().method {
//                        name = "getContentResolver";emptyParam();superClass()
//                    }.invoke<ContentResolver>() ?: return@afterHook
//                    val mSpeedLevelObserver =
//                        field { name = "mSpeedLevelObserver" }.get(instance).cast<ContentObserver>()
//                            ?: return@afterHook
//                    contentResolver.unregisterContentObserver(mSpeedLevelObserver)
//                }
//            }
//        }

//        findClass("com.android.launcher3.anim.AppLaunchAnimSpeedHandler\$Companion").hook {
//            injectMember {
//                method { name = "isNotSupportSpeedModify" }
//                replaceToFalse()
//            }
//            injectMember {
//                method { name = "parseSpeedLevel" }
//                beforeHook {
//                    args().last().setTrue()
//                }
//                afterHook {
//                    val context = args().first().cast<Context>() ?: return@afterHook
//                    val str = args(1).string()
//                    val bool = args().last().boolean()
//                    loggerD(msg = "parseSpeedLevel -> $str $bool -> $result")
//                    if (bool && str == "2") {
//                        Settings.Secure.putString(
//                            context.contentResolver, "setting_app_startup_anim_speed", str
//                        )
//                    }
//                    result = str.toInt()
//                }
//            }
//        }
//
//        "com.android.launcher3.OplusLauncherAppTransitionValueAnimator".toClass().apply {
//            val DURATION_MS = field { name = "DURATION_MS" }.get().cast<LongArray>() ?: return@apply
//            val newArray = DURATION_MS.toMutableList().apply { add(850L) }
//            field { name = "DURATION_MS" }.get().set(newArray.toLongArray())
//        }

        //com.android.quickstep.views.OplusTaskMenuViewImpl
        //res/layout/oplus_task_menu_option.xml

        //<string name="oplus_shortcut_lock_app">锁定</string>
        //<string name="oplus_shortcut_locked_app">解锁</string>
        //<string name="oplus_rapid_reach_float_window">浮窗</string>
        //<string name="recent_task_option_split_screen">分屏</string>
        //<string name="oplus_privacy_not_show_preview">隐藏内容</string>
        //<string name="oplus_privacy_show_preview">显示内容</string>
        //<string name="oplus_shortcut_lock_setting">管理</string>

        //com.android.launcher3.popup.OplusBaseSystemShortcut
        //OplusAppInfo etc. -> Click

        //AppEdit
        //com.oplus.uxicon.ui.ui.UxEditPanelFragment -> res/layout/edit_panel_layout.xml
        //<string name="no_icon_pack_toast">没有支持替换图标的图标包</string>
    }
}