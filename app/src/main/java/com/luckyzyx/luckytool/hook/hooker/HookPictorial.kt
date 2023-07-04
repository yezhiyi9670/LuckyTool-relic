package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.pictorial.RemoveImageSaveWaterMark
import com.luckyzyx.luckytool.hook.scope.pictorial.RemoveVideoSaveWaterMark
import com.luckyzyx.luckytool.utils.ModulePrefs

object HookPictorial : YukiBaseHooker() {
    override fun onHook() {
        //移除图片保存水印
        if (prefs(ModulePrefs).getBoolean("remove_image_save_watermark", false)) {
            loadHooker(RemoveImageSaveWaterMark)
        }
        //移除视频保存水印
        if (prefs(ModulePrefs).getBoolean("remove_video_save_watermark", false)) {
            loadHooker(RemoveVideoSaveWaterMark)
        }
    }
}