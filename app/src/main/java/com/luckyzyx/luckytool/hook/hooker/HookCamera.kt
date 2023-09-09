package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.camera.CustomModelWaterMark
import com.luckyzyx.luckytool.hook.scope.camera.EnableMasterFilter
import com.luckyzyx.luckytool.hook.scope.camera.HookCameraConfig
import com.luckyzyx.luckytool.hook.scope.camera.RemoveWatermarkWordLimit
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.getAppSet

object HookCamera : YukiBaseHooker() {
    override fun onHook() {
        val appSet = getAppSet(ModulePrefs, packageName)
        if (appSet[2] == "null") return

        //HookCameraConfig
        if (SDK >= A13) loadHooker(HookCameraConfig)

        //自定义机型水印
        if (SDK >= A13) loadHooker(CustomModelWaterMark)

        //移除水印字数限制
        if (prefs(ModulePrefs).getBoolean("remove_watermark_word_limit", false)) {
            loadHooker(RemoveWatermarkWordLimit)
        }

        //大师滤镜
        if (prefs(ModulePrefs).getBoolean("enable_master_filter", false)) {
            if (SDK >= A13) loadHooker(EnableMasterFilter)
        }

        //修复哈苏定制水印崩溃
//        if (prefs(ModulePrefs).getBoolean("fix_hasselblad_custom_watermark_crash", false)) {
//            loadHooker(FixHasselbladCustomWatermarkCrash)
//        }
    }
}