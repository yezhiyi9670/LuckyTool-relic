package com.luckyzyx.luckytool.utils

import com.highcapable.yukihookapi.hook.log.loggerD
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

object DexkitUtils {
    /**
     * 创建Dexkit实例
     * @param appPath String
     * @return DexKitBridge?
     */
    fun create(appPath: String): DexKitBridge? {
        System.loadLibrary("dexkit")
        return DexKitBridge.create(appPath)
    }

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver ClassDataList
     * @param instance String
     */
    fun ClassDataList.printLog(instance: String) {
        val msg = if (isNullOrEmpty()) "$instance -> findClass isNullOrEmpty"
        else if (size != 1) {
            var f = ""
            forEach { f += "[${it.className}]" }
            "$instance -> findClass size ($size) -> $f"
        } else ""//"findclass ${first().className}"
        if (msg.isNotBlank()) loggerD("LuckyTool", msg)
    }
}