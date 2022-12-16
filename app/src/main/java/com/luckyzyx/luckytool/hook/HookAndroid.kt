package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.*

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())

        //移除VPN已激活通知
        loadHooker(RemoveVPNActiveNotification())

        //媒体音量阶数
        loadHooker(MediaVolumeLevel())

        //应用分身限制
        loadHooker(MultiApp())

        //USB安装确认
        loadHooker(ADBInstallConfirm())

        //移除72小时密码验证
        loadHooker(Remove72HourPasswordVerification())

        //移除系统截屏延迟
        loadHooker(RemoveSystemScreenshotDelay())

        //移除遮罩Splash Screen
        loadHooker(AppSplashScreen())

        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())
    }
}
