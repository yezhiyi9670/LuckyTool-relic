package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.screenshot.CustomizeLongScreenshotMaxCapturedPages
import com.luckyzyx.luckytool.hook.scope.screenshot.EnablePNGSaveFormat
import com.luckyzyx.luckytool.hook.scope.screenshot.RemoveScreenshotPrivacyLimit
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getAppSet

object HookScreenshot : YukiBaseHooker() {
    override fun onHook() {
        val appSet = getAppSet(ModulePrefs, packageName)
        //移除截屏隐私限制
        if (prefs(ModulePrefs).getBoolean("remove_screenshot_privacy_limit", false)) {
            loadHooker(RemoveScreenshotPrivacyLimit)
        }
        //移除长截图页数限制
        if (prefs(ModulePrefs).getBoolean("remove_page_limit_for_long_screenshots", false)) {
            val exist = appSet[1].toIntOrNull()?.let { it > 130005000 } ?: false
            if (exist) loadHooker(CustomizeLongScreenshotMaxCapturedPages)
        }
        //启用PNG保存格式
        if (prefs(ModulePrefs).getBoolean("enable_png_save_format", false)) {
            loadHooker(EnablePNGSaveFormat)
        }
    }
}