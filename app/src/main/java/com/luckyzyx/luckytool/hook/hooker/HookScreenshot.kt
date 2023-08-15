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
            if (newVer != null) loadHooker(CustomizeLongScreenshotMaxCapturedPages)
            else {
                loggerD(msg = "此截屏版本不支持移除长截图页数限制,请关闭此功能或者更换截屏版本!")
                loggerD(msg = "This screenshot version does not support removing the page limit for long screenshots, please disable this function or replace the screenshot version!")
            }
        }
        //启用PNG保存格式
        if (prefs(ModulePrefs).getBoolean("enable_png_save_format", false)) {
            loadHooker(EnablePNGSaveFormat)
        }
    }
}