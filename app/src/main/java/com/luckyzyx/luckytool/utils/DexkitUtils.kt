package com.luckyzyx.luckytool.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import com.luckyzyx.luckytool.hook.scope.gallery.ReplaceOnePlusModelWaterMark
import com.luckyzyx.luckytool.hook.scope.oplusgames.RemoveRootCheck
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.ClassDataList

@Suppress("unused", "MemberVisibilityCanBePrivate")
class DexkitUtils(val context: Context, val packName: String) {
    var appInfo: ApplicationInfo
    var appPath: String
    var bridge: DexKitBridge? = null

    init {
        System.loadLibrary("dexkit")
        appInfo = PackageUtils(context.packageManager).getApplicationInfo(packName, 0)
        appPath = appInfo.sourceDir
    }

    fun start(): String {
        val result = when (packName) {
            "com.oplus.games" -> {
                arraySummaryLine(
                    RemoveRootCheck.let {
                        it.searchDexkit(appPath).printValue("RemoveRootCheck", it.key)
                    }
                )
            }

            "com.coloros.gallery3d" -> {
                arraySummaryLine(
                    ReplaceOnePlusModelWaterMark.let {
                        it.searchDexkit(appPath).printValue("ReplaceOnePlusModelWaterMark", it.key)
                    }
                )
            }

            else -> ""
        }
        return result
    }

    /**
     * 创建Dexkit实例
     * @param tag String
     * @return DexKitBridge?
     */
    fun create(tag: String): DexKitBridge? {
        return DexKitBridge.create(appPath) ?: run {
            LogUtils.e(tag, "create", "DexKitBridge.create() failed")
            null
        }
    }

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver ClassDataList
     * @param tag String
     * @param key String
     */
    fun ClassDataList.printValue(tag: String, key: String): String {
        var msg = ""
        if (isNullOrEmpty()) {
            msg = "$tag -> ClassDataList isNullOrEmpty"
            LogUtils.e(tag, "searchDexkit", msg)
        } else if (size != 1) {
            msg = "$tag -> findClass size == $size"
            LogUtils.e(tag, "searchDexkit", msg)
            forEachIndexed { index, classData ->
                LogUtils.d(tag, "searchDexkit", "$index -> ${classData.className}")
            }
        } else {
            context.putString(DexkitPrefs, key, first().className)
            LogUtils.d(tag, "searchDexkit", "findClass ${first().className}")
        }
        return msg
    }
}