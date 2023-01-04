package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookScreenshot : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.oplus.screenshot"){
            //移除截屏隐私限制
            if (prefs(XposedPrefs).getBoolean("remove_screenshot_privacy_limit",false)) loadHooker(
                RemoveScreenshotPrivacyLimit()
            )
        }
    }
}