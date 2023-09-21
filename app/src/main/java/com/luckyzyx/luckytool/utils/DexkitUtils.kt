package com.luckyzyx.luckytool.utils

import com.highcapable.yukihookapi.hook.log.loggerD
import com.luckyzyx.luckytool.BuildConfig
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

object DexkitUtils {
    val debug = BuildConfig.DEBUG

    /**
     * 创建Dexkit实例
     * @param appPath String
     * @return DexKitBridge?
     */
    private fun create(appPath: String): DexKitBridge? {
        System.loadLibrary("dexkit")
        return DexKitBridge.create(appPath)
    }

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver ClassDataList
     * @param instance String
     */
    private fun ClassDataList?.printLog(instance: String) {
        val msg = if (isNullOrEmpty()) "$instance -> findClass isNullOrEmpty"
        else if (size != 1) {
            var f = ""
            forEach { f += "[${it.className}]" }
            "$instance -> findClass size ($size) -> $f"
        } else if (debug) "$instance -> findclass ${first().className}" else ""
        if (msg.isNotBlank()) loggerD("LuckyTool", msg)
    }

    /**
     * 使用规则查找Class
     * @param tag String
     * @param appPath String
     * @param initialization Function1<DexKitBridge, ClassDataList>
     * @return ClassDataList?
     */
    fun searchDexClass(
        tag: String,
        appPath: String,
        initialization: (DexKitBridge) -> ClassDataList?
    ): ClassDataList? {
        val result = create(appPath)?.use { initialization(it) }
        if (tag.isBlank()) result.printLog(appPath)
        else result.printLog(tag)
        return result
    }
}