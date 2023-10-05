package com.luckyzyx.luckytool.utils

import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.log.loggerE
import com.luckyzyx.luckytool.BuildConfig
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList
import org.luckypray.dexkit.query.MethodDataList

@Suppress("MemberVisibilityCanBePrivate")
object DexkitUtils {
    const val tag = "LuckyTool"
    val debug = BuildConfig.DEBUG

    /**
     * 创建Dexkit实例
     * @param appPath String
     * @return DexKitBridge?
     */
    fun create(appPath: String): DexKitBridge? {
        System.loadLibrary("dexkit")
        return DexKitBridge.create(appPath)
    }

    fun create(appPath: String, result: (DexKitBridge) -> Unit) {
        System.loadLibrary("dexkit")
        DexKitBridge.create(appPath)?.use { result(it) }
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

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver ClassDataList
     * @param instance String
     */
    fun ClassDataList?.printLog(instance: String): ClassDataList? {
        if (isNullOrEmpty()) {
            loggerE(tag, "$instance -> findClass isNullOrEmpty")
        } else if (size != 1) {
            var f = ""
            forEach { f += "[${it.className}]" }
            loggerE(tag, "$instance -> findClass size ($size) -> $f")
        } else if (debug) {
            loggerD(tag, "$instance -> findclass ${first().className}")
        }
        return this
    }

    /**
     * 使用规则查找Method
     * @param tag String
     * @param appPath String
     * @param initialization Function1<DexKitBridge, ClassDataList>
     * @return MethodDataList?
     */
    fun searchDexMethod(
        tag: String,
        appPath: String,
        initialization: (DexKitBridge) -> MethodDataList?
    ): MethodDataList? {
        val result = create(appPath)?.use { initialization(it) }
        if (tag.isBlank()) result.printLog(appPath)
        else result.printLog(tag)
        return result
    }

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver MethodDataList
     * @param instance String
     */
    fun MethodDataList?.printLog(instance: String): MethodDataList? {
        if (isNullOrEmpty()) {
            loggerE(tag, "$instance -> findMethod isNullOrEmpty")
        } else if (debug) {
            var f = ""
            forEach { f += "[${it.className} - ${it.methodName}]" }
            loggerD(tag, "$instance -> findMethod size ($size) -> $f")
        }
        return this
    }
}