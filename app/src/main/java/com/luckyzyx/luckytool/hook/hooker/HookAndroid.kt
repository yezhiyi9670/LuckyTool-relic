package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.android.ADBInstallConfirm
import com.luckyzyx.luckytool.hook.scope.android.AllowUntrustedTouch
import com.luckyzyx.luckytool.hook.scope.android.AppSplashScreen
import com.luckyzyx.luckytool.hook.scope.android.BatteryOptimizationWhitelist
import com.luckyzyx.luckytool.hook.scope.android.DarkModeService
import com.luckyzyx.luckytool.hook.scope.android.DisableFlagSecure
import com.luckyzyx.luckytool.hook.scope.android.HookNotificationManager
import com.luckyzyx.luckytool.hook.scope.android.HookWindowManagerService
import com.luckyzyx.luckytool.hook.scope.android.MediaVolumeLevel
import com.luckyzyx.luckytool.hook.scope.android.MultiApp
import com.luckyzyx.luckytool.hook.scope.android.RemovePasswordTimeoutVerification
import com.luckyzyx.luckytool.hook.scope.android.RemoveStatusBarTopNotification
import com.luckyzyx.luckytool.hook.scope.android.RemoveSystemScreenshotDelay
import com.luckyzyx.luckytool.hook.scope.android.RemoveVPNActiveNotification
import com.luckyzyx.luckytool.hook.scope.android.ZoomWindow


object HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification)

        //移除VPN已激活通知
        loadHooker(RemoveVPNActiveNotification)

        //Hook NotificationManager
        loadHooker(HookNotificationManager)

        //Hook HookWindowManagerService
        loadHooker(HookWindowManagerService)

        //HookSystemProperties
//        loadHooker(HookSystemProperties)

        //媒体音量阶数
        loadHooker(MediaVolumeLevel)

        //应用分身限制
        loadHooker(MultiApp)

        //USB安装确认
        loadHooker(ADBInstallConfirm)

        //移除72小时密码验证
        loadHooker(RemovePasswordTimeoutVerification)

        //移除系统截屏延迟
        loadHooker(RemoveSystemScreenshotDelay)

        //移除遮罩Splash Screen
        loadHooker(AppSplashScreen)

        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure)

        //允许不受信任的触摸
        loadHooker(AllowUntrustedTouch)

        //缩放窗口
        loadHooker(ZoomWindow)

        //暗色模式服务
        loadHooker(DarkModeService)

        //电池优化白名单
        loadHooker(BatteryOptimizationWhitelist)
//
//        findClass("com.android.server.display.OplusFeatureDCBacklight").hook {
//            injectMember {
//                method { name = "updateDCBacklight" }
//                beforeHook {
//                    val mIsFeatureOn = field { name = "mIsFeatureOn" }.get(instance).boolean()
//                    val mCurrentDCMode = field { name = "mCurrentDCMode" }.get(instance).int()
//                    val mBtExtendSupported =
//                        field { name = "mBtExtendSupported" }.get(instance).int()
//                    val mIsApollo = field { name = "mIsApollo" }.get(instance).boolean()
//                    val mIsPwDCModeSupport =
//                        field { name = "mIsPwDCModeSupport" }.get(instance).boolean()
//
//                    loggerD(
//                        msg = "mIsFeatureOn -> $mIsFeatureOn" +
//                                "mCurrentDCMode -> $mCurrentDCMode" +
//                                "mBtExtendSupported -> $mBtExtendSupported" +
//                                "mIsApollo -> $mIsApollo" +
//                                "mIsPwDCModeSupport -> $mIsPwDCModeSupport"
//                    )
//                    val mode = args().first().cast<Int>() ?: return@beforeHook
//                    method { name = "applyApolloDCMode" }.get(instance).call(mode)
//                    method { name = "applyDCMode" }.get(instance).call(mode)
//
//                }
//            }
//        }

        //201850903 0x0c080017 oplus_ic_corp_icon_badge_multiapp
        //201850911 0x0c08001f oplus_ic_corp_badge_case_multiapp
        //201850912 0x0c080020 oplus_ic_corp_badge_no_background_multiapp
//        findClass("android.util.IconDrawableFactory").hook {
//            injectMember {
//                method {
//                    name = "getBadgedIcon"
//                    paramCount = 3
//                }
//                intercept()
//            }
//        }
    }
}
