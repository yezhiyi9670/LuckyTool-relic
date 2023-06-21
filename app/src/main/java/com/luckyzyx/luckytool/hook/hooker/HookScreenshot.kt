package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.luckytool.hook.scope.screenshot.CustomizeLongScreenshotMaxCapturedPages
import com.luckyzyx.luckytool.hook.scope.screenshot.EnablePNGSaveFormat
import com.luckyzyx.luckytool.hook.scope.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookScreenshot : YukiBaseHooker() {
    override fun onHook() {
        //移除截屏隐私限制
        if (prefs(ModulePrefs).getBoolean("remove_screenshot_privacy_limit", false)) {
            loadHooker(RemoveScreenshotPrivacyLimit)
        }
        //移除长截图页数限制
        if (prefs(ModulePrefs).getBoolean("remove_page_limit_for_long_screenshots", false)) {
            val newVer = "com.oplus.providers.AppSettings".toClassOrNull()
            if (newVer == null) loggerD(msg = "$packageName\n移除长截图页数限制: 不支持此截屏版本\nRemove page limit for long screenshots: this screenshot version is not supported.")
            else loadHooker(CustomizeLongScreenshotMaxCapturedPages)
        }
        //启用PNG保存格式
        if (prefs(ModulePrefs).getBoolean("enable_png_save_format", false)) {
            loadHooker(EnablePNGSaveFormat)
        }
    }
}