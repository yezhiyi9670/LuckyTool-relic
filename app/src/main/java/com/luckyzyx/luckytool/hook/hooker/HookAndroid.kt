package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.android.*


object HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification)

        //移除VPN已激活通知
        loadHooker(RemoveVPNActiveNotification)

        //Hook NotificationManager
        loadHooker(HookNotificationManager)

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

        //201850903 0x0c080017 oplus_ic_corp_icon_badge_multiapp
        //201850911 0x0c08001f oplus_ic_corp_badge_case_multiapp
        //201850912 0x0c080020 oplus_ic_corp_badge_no_background_multiapp
//        findClass("android.app.ApplicationPackageManager").hook {
//            injectMember {
//                method {
//                    name = "getUserBadgedIcon"
//                }
//                replaceAny {
//                    args().first().cast<Drawable>()
//                }
//            }
//        }

        //OplusFeature
//        loadHooker(OplusFeature)

        //com.android.server.policy.PhoneWindowManager
//        findClass("com.android.server.policy.PhoneWindowManager").hook {
//            injectMember {
//                method {
//                    name = "init"
//                    paramCount = 3
//                }
//                afterHook {
//                    val time = field {
//                        name = "mLongPressOnPowerAssistantTimeoutMs"
//                    }.get(instance).long()
//                    loggerD(msg = "mLongPressOnPowerAssistantTimeoutMs -> $time")
//                }
//            }
//        }
    }
}
