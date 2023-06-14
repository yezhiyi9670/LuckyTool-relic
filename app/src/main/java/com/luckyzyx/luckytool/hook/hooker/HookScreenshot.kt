package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.screenshot.CustomizeLongScreenshotMaxCapturedPages
import com.luckyzyx.luckytool.hook.scope.screenshot.CustomizeLongScreenshotMaxCapturedPages131
import com.luckyzyx.luckytool.hook.scope.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getOSVersion

object HookScreenshot : YukiBaseHooker() {
    override fun onHook() {
        //移除截屏隐私限制
        if (prefs(ModulePrefs).getBoolean("remove_screenshot_privacy_limit", false)) {
            loadHooker(RemoveScreenshotPrivacyLimit)
        }
        //自定义长截图最大捕获页数
        if (prefs(ModulePrefs).getInt("customize_long_screenshot_max_captured_pages", 18) > 18) {
            if (getOSVersion() < 13.1) loadHooker(CustomizeLongScreenshotMaxCapturedPages)
            else loadHooker(CustomizeLongScreenshotMaxCapturedPages131)
        }
    }
}