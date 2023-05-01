package com.luckyzyx.luckytool.hook

import android.os.Build.VERSION_CODES.R
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.S_V2
import android.os.Build.VERSION_CODES.TIRAMISU
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.luckyzyx.luckytool.hook.hooker.HookAndroid
import com.luckyzyx.luckytool.hook.hooker.HookApplication
import com.luckyzyx.luckytool.hook.hooker.HookAutoStart
import com.luckyzyx.luckytool.hook.hooker.HookBattery
import com.luckyzyx.luckytool.hook.hooker.HookCamera
import com.luckyzyx.luckytool.hook.hooker.HookCloudService
import com.luckyzyx.luckytool.hook.hooker.HookDesktop
import com.luckyzyx.luckytool.hook.hooker.HookDialogRelated
import com.luckyzyx.luckytool.hook.hooker.HookFingerPrintRelated
import com.luckyzyx.luckytool.hook.hooker.HookGestureRelated
import com.luckyzyx.luckytool.hook.hooker.HookLockScreen
import com.luckyzyx.luckytool.hook.hooker.HookMiscellaneous
import com.luckyzyx.luckytool.hook.hooker.HookNotificationManager
import com.luckyzyx.luckytool.hook.hooker.HookOplusGames
import com.luckyzyx.luckytool.hook.hooker.HookOplusOta
import com.luckyzyx.luckytool.hook.hooker.HookOtherApp
import com.luckyzyx.luckytool.hook.hooker.HookPackageInstaller
import com.luckyzyx.luckytool.hook.hooker.HookScreenshot
import com.luckyzyx.luckytool.hook.hooker.HookSettings
import com.luckyzyx.luckytool.hook.hooker.HookStatusBar
import com.luckyzyx.luckytool.hook.hooker.HookThemeStore
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForR
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForS
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForSv2
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForT
import com.luckyzyx.luckytool.hook.statusbar.StatusBarBattery
import com.luckyzyx.luckytool.hook.statusbar.StatusBarClock
import com.luckyzyx.luckytool.hook.statusbar.StatusBarControlCenter
import com.luckyzyx.luckytool.hook.statusbar.StatusBarIcon
import com.luckyzyx.luckytool.hook.statusbar.StatusBarLayout
import com.luckyzyx.luckytool.hook.statusbar.StatusBarNetWorkSpeed
import com.luckyzyx.luckytool.hook.statusbar.StatusBarNotify
import com.luckyzyx.luckytool.hook.statusbar.StatusBarTile
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.SettingsPrefs
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

@InjectYukiHookWithXposed(isUsingResourcesHook = false)
object MainHook : IYukiHookXposedInit {
    override fun onInit() = configs {
        debugLog {
            tag = "LuckyTool"
            isEnable = true
            isRecord = true
            elements(TAG, PRIORITY, PACKAGE_NAME, USER_ID)
        }
        isDebug = false
    }

    override fun onHook() = encase {
        if (prefs(ModulePrefs).getBoolean("enable_module").not()) return@encase
        if (prefs(SettingsPrefs).getBoolean("is_su").not()) return@encase
        //系统框架
        loadSystem(HookAndroid)

        //状态栏功能
        loadApp("com.android.systemui", HookStatusBar)
        //状态栏时钟
        loadApp("com.android.systemui", StatusBarClock)
        //状态栏网速
        loadApp("com.android.systemui", StatusBarNetWorkSpeed)
        //状态栏通知
        loadApp(
            "com.android.systemui",
            "com.oplus.battery",
            "com.coloros.phonemanager"
        ) {
            loadHooker(StatusBarNotify)
        }
        //状态栏通知限制
        loadApp("com.oplus.notificationmanager", HookNotificationManager)

        //状态栏图标
        loadApp("com.android.systemui", StatusBarIcon)
        //状态栏控制中心
        loadApp("com.android.systemui", StatusBarControlCenter)
        //状态栏磁贴
        loadApp("com.android.systemui", StatusBarTile)
        //状态栏布局
        loadApp("com.android.systemui", StatusBarLayout)
        //状态栏电池
        loadApp("com.android.systemui", StatusBarBattery)

        //桌面
        loadApp("com.coloros.alarmclock", "com.android.launcher") {
            loadHooker(HookDesktop)
        }

        //锁屏
        loadApp("com.android.systemui", HookLockScreen)
        //截屏
        loadApp("com.oplus.screenshot", HookScreenshot)
        //应用
        loadApp(
            "com.oplus.battery",
            "com.oplus.safecenter",
            "com.coloros.safecenter",
            "com.android.launcher",
            "com.oppo.launcher"
        ) {
            loadHooker(HookApplication)
        }
        //应用安装器
        loadApp("com.android.packageinstaller", HookPackageInstaller)
        //对话框相关
        loadApp(
            "com.android.systemui",
            "com.oplus.exsystemservice",
            "com.coloros.securepay"
        ) {
            loadHooker(HookDialogRelated)
        }
        //全面屏手势相关
        loadApp("com.android.systemui", HookGestureRelated)
        //指纹相关
        loadApp("com.android.systemui", HookFingerPrintRelated)
        //杂项
        loadApp(
            "com.android.systemui",
            "com.android.externalstorage"
        ) {
            loadHooker(HookMiscellaneous)
        }

        //设置
        loadApp("com.android.settings", HookSettings)
        //电池
        loadApp("com.oplus.battery", HookBattery)
        //相机
        loadApp("com.oneplus.camera", "com.oplus.camera") {
            loadHooker(HookCamera)
        }
        //主题商店
        loadApp("com.heytap.themestore", "com.oplus.themestore") {
            loadHooker(HookThemeStore)
        }
        //云服务
        loadApp("com.heytap.cloud", HookCloudService)
        //游戏助手
        loadApp("com.oplus.games", "com.oplus.cosa") {
            loadHooker(HookOplusGames)
        }
        //软件更新
        loadApp("com.oplus.ota", HookOplusOta)

        //其他APP
        loadApp(
            "com.east2d.everyimage",
            "com.ruet_cse_1503050.ragib.appbackup.pro",
            "ru.kslabs.ksweb"
        ) {
            loadHooker(HookOtherApp)
        }

        //自启
        loadApp("com.android.systemui", HookAutoStart)
    }

    override fun onXposedEvent() {
        YukiXposedEvent.onHandleLoadPackage { lpparam: XC_LoadPackage.LoadPackageParam ->
            run {
                if (lpparam.packageName == "android" && lpparam.processName == "android") {
                    when (SDK) {
                        TIRAMISU -> CorePatchForT().handleLoadPackage(lpparam)
                        S_V2 -> CorePatchForSv2().handleLoadPackage(lpparam)
                        S -> CorePatchForS().handleLoadPackage(lpparam)
                        R -> CorePatchForR().handleLoadPackage(lpparam)
                        else -> loggerE(msg = "[CorePatch] Unsupported Version of Android -> $SDK")
                    }
                }
            }
        }
        YukiXposedEvent.onInitZygote { startupParam: IXposedHookZygoteInit.StartupParam ->
            run {
                when (SDK) {
                    TIRAMISU -> CorePatchForT().initZygote(startupParam)
                    S_V2 -> CorePatchForSv2().initZygote(startupParam)
                    S -> CorePatchForS().initZygote(startupParam)
                    R -> CorePatchForR().initZygote(startupParam)
                    else -> loggerE(msg = "[CorePatch] Unsupported Version of Android -> $SDK")
                }
            }
        }
    }
}