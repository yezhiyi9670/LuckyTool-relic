package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object HookScreenshot : YukiBaseHooker() {
    override fun onHook() {
        //移除截屏隐私限制
        if (prefs(ModulePrefs).getBoolean("remove_screenshot_privacy_limit", false)) {
            loadHooker(RemoveScreenshotPrivacyLimit)
        }
    }
}