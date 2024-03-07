package com.luckyzyx.luckytool.utils

import com.highcapable.yukihookapi.hook.log.YLog
import com.luckyzyx.luckytool.BuildConfig
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList
import org.luckypray.dexkit.query.MethodDataList

@Suppress("MemberVisibilityCanBePrivate")
object DexkitUtils {
    const val tag = "LuckyTool"
    val debug = BuildConfig.DEBUG

    /**
     * 创建Dexkit安全实例
     * @param appPath String
     * @param result Function1<DexKitBridge, Unit>
     */
    fun create(appPath: String, result: (DexKitBridge) -> Unit) {
        System.loadLibrary("dexkit")
        DexKitBridge.create(appPath)?.use { result(it) }
    }

    /**
     * 检查搜索到的类列表并打印LOG
     * @receiver ClassDataList
     * @param instance String
     * @param onlyOne Boolean
     * @return ClassDataList
     */
    fun ClassDataList.checkDataList(instance: String, onlyOne: Boolean = true): ClassDataList {
        when {
            isNullOrEmpty() -> YLog.error("$instance -> findMethod isNullOrEmpty", tag = tag)
            size > 1 && onlyOne -> {
                var find = ""
                forEach { find += "[${it.name}]" }
                YLog.error("$instance -> findMethod size ($size) -> $find", tag = tag)
            }

            size == 1 -> if (debug) YLog.debug(
                "$instance -> findMethod ${first().name}",
                tag = tag
            )
        }
        return this
    }

    /**
     * 检查搜索到的方法列表并打印LOG
     * @receiver MethodDataList
     * @param instance String
     * @param onlyOne Boolean
     * @return MethodDataList
     */
    fun MethodDataList.checkDataList(instance: String, onlyOne: Boolean = true): MethodDataList {
        when {
            isNullOrEmpty() -> YLog.error("$instance -> findMethod isNullOrEmpty", tag = tag)
            size > 1 && onlyOne -> {
                var find = ""
                forEach { find += "[${it.className}|${it.methodName}]" }
                YLog.error("$instance -> findMethod size ($size) -> $find", tag = tag)
            }

            size == 1 -> if (debug) YLog.debug(
                "$instance -> findMethod ${first().className}|${first().methodName}",
                tag = tag
            )
        }
        return this
    }
}