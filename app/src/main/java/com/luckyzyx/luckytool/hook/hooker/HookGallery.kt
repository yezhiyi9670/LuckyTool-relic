package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.gallery.EnableWatermarkEditing
import com.luckyzyx.luckytool.hook.scope.gallery.ReplaceOnePlusModelWaterMark
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.getAppSet

object HookGallery : YukiBaseHooker() {
    override fun onHook() {
        val appSet = getAppSet(ModulePrefs, packageName)
        if (appSet[1].toIntOrNull()?.let { it < 13005000 } == true) return

        //启用哈苏水印编辑
        if (prefs(ModulePrefs).getBoolean("enable_watermark_editing", false)) {
            loadHooker(EnableWatermarkEditing)
        }

        //替换OnePlus机型水印
        loadHooker(ReplaceOnePlusModelWaterMark)

    }
}