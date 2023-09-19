package com.luckyzyx.luckytool.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import com.luckyzyx.luckytool.hook.scope.gallery.EnableWatermarkEditing
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

    fun start() {
        when (packName) {
            "com.oplus.games" -> {
                bridge = create(packName)
                RemoveRootCheck.apply {
                    searchDexkit(bridge).printValue("RemoveRootCheck", key)
                }
            }

            "com.coloros.gallery3d" -> {
                bridge = create(packName)
                EnableWatermarkEditing.apply {
                    searchDexkit(bridge).printValue("EnableWatermarkEditing", key)
                }
                ReplaceOnePlusModelWaterMark.apply {
                    searchDexkit(bridge).printValue("ReplaceOnePlusModelWaterMark", key)
                }
            }
        }
        bridge?.close()
    }

    /**
     * 创建Dexkit实例
     * @param tag String
     * @return DexKitBridge?
     */
    fun create(tag: String): DexKitBridge? {
        return try {
            DexKitBridge.create(appPath) ?: kotlin.run {
                LogUtils.e(tag, "create", "DexKitBridge.create() failed")
                null
            }
        } catch (e: Throwable) {
            LogUtils.e(tag, "create catch", "DexKitBridge.create() failed")
            null
        }
    }

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver ClassDataList
     * @param tag String
     * @param key String
     */
    fun ClassDataList?.printValue(tag: String, key: String) {
        if (isNullOrEmpty()) LogUtils.e(tag, "searchDexkit", "ClassDataList isNullOrEmpty")
        else if (size != 1) {
            LogUtils.e(tag, "searchDexkit", "findClass size == $size")
            forEachIndexed { index, classData ->
                LogUtils.d(tag, "searchDexkit", "$index -> ${classData.className}")
            }
        } else {
            context.putString(DexkitPrefs, key, first().className)
            LogUtils.d(tag, "searchDexkit", "findClass ${first().className}")
        }
    }
}