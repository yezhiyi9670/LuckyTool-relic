package com.luckyzyx.luckytool.hook

import android.os.Build.VERSION_CODES.*
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.luckyzyx.luckytool.hook.hooker.*
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForR
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForS
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForSv2
import com.luckyzyx.luckytool.hook.scope.CorePatch.CorePatchForT
import com.luckyzyx.luckytool.hook.statusbar.*
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

@InjectYukiHookWithXposed
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
        if (prefs(XposedPrefs).getBoolean("enable_module").not()) return@encase
        //系统框架
        loadSystem(HookAndroid)
        loadZygote(HookZygote)

        //状态栏时钟
        loadApp(hooker = StatusBarClock)
        //状态栏网速
        loadApp(hooker = StatusBarNetWorkSpeed)
        //状态栏通知
        loadApp(hooker = StatusBarNotice)
        //状态栏图标
        loadApp(hooker = StatusBarIcon)
        //状态栏控制中心
        loadApp(hooker = StatusBarControlCenter)

        //状态栏电池通知
        loadApp(hooker = BatteryInfoNotify)

        //桌面
        loadApp(hooker = HookDesktop)
        //锁屏
        loadApp(hooker = HookLockScreen)
        //截屏
        loadApp(hooker = HookScreenshot)
        //应用
        loadApp(hooker = HookApplication)
        //应用安装器
        loadApp(hooker = HookPackageInstaller)
        //对话框相关
        loadApp(hooker = HookDialogRelated)
        //全面屏手势相关
        loadApp(hooker = HookGestureRelated)
        //杂项
        loadApp(hooker = HookMiscellaneous)

        //相机
        loadApp("com.oplus.camera", HookCamera)
        //主题商店
        loadApp("com.heytap.themestore", HookThemeStore)
        //云服务
        loadApp("com.heytap.cloud", HookCloudService)
        //游戏助手
        loadApp("com.oplus.games", HookOplusGames)
        //软件更新
        loadApp("com.oplus.ota", HookOplusOta)

        //其他APP
        loadApp(hooker = HookOtherApp)

        //自动强制FPS
        loadApp(hooker = HookAutoFps)

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