package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.systemui.DoubleClickLockScreen
import com.luckyzyx.luckytool.hook.scope.systemui.HideInActiveSignalLabelsGen2x2
import com.luckyzyx.luckytool.hook.scope.systemui.VibrateWhenOpeningTheStatusBar
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookStatusBar : YukiBaseHooker() {
    override fun onHook() {
        //双击状态栏锁屏
        if (prefs(ModulePrefs).getBoolean("statusbar_double_click_lock_screen", false)) {
            loadHooker(DoubleClickLockScreen)
        }
        //打开状态栏时振动
        if (prefs(ModulePrefs).getBoolean("vibrate_when_opening_the_statusbar", false)) {
            loadHooker(VibrateWhenOpeningTheStatusBar)
        }
        //隐藏未使用信号标签
        if (prefs(ModulePrefs).getBoolean("hide_inactive_signal_labels_gen2x2", false)) {
            loadHooker(HideInActiveSignalLabelsGen2x2)
        }

        //res/layout/bubble_expanded_view.xml
        //<string name="bubble_close">关闭对话</string>
        //<string name="open_app">进入应用</string>
        //flag_conversations
        //bubble_ic_create_bubble 箭头向右下
        //bubble_ic_stop_bubble 箭头向左上

//        "com.android.wm.shell.bubbles.BubbleController".toClass().apply {
//            method { name = "isResizableActivity" }.hook {
//                replaceToTrue()
//            }
//        }
//
//        "com.android.systemui.statusbar.notification.row.NotificationContentView".toClass().apply {
//            method { name = "shouldShowBubbleButton" }.hook {
//                before {
//                    val entry = args().first().any() ?: return@before
//                    result = entry.current().method {
//                        name = "getBubbleMetadata";emptyParam()
//                    }.call() != null
//                    YLog.debug("${method.name} -> $result")
//                }
//            }
//        }
//
//        "com.android.systemui.wmshell.BubblesManager".toClass().apply {
//            method { name = "areBubblesEnabled" }.hook {
//                replaceToTrue()
//            }
//        }

    }
}
