package com.luckyzyx.luckytool.utils

import android.content.Context
import com.luckyzyx.luckytool.hook.dexkit.OplusGallery
import com.luckyzyx.luckytool.hook.dexkit.OplusGame
import org.luckypray.dexkit.query.ClassDataList

@Suppress("unused", "MemberVisibilityCanBePrivate")
object DexkitUtils {

    init {
        System.loadLibrary("dexkit")
    }

    fun start(context: Context, packName: String): String {
        val appInfo = PackageUtils(context.packageManager).getApplicationInfo(packName, 0)
        val result = when (packName) {
            "com.oplus.games" -> OplusGame.get(context, appInfo.sourceDir)
            "com.coloros.gallery3d" -> OplusGallery.get(context, appInfo.sourceDir)
            else -> ""
        }
        return result
    }

    /**
     * 根据搜索数量处理LOG与缓存
     * @receiver ClassDataList
     * @param tag String
     * @param key String
     */
    fun ClassDataList.printValue(context: Context, tag: String, key: String): String {
        val msg: String
        if (isNullOrEmpty()) {
            msg = "$tag -> findClass isNullOrEmpty"
            LogUtils.e(tag, "searchDexkit", msg)
        } else if (size != 1) {
            msg = "$tag -> findClass size == $size"
            LogUtils.e(tag, "searchDexkit", msg)
            forEachIndexed { index, classData ->
                LogUtils.d(tag, "searchDexkit", "$index -> ${classData.className}")
            }
        } else {
            val value = safeOf("null") { first().className }
            val get = context.getString(DexkitPrefs, key, "null")
            msg = if (get != value) context.putString(DexkitPrefs, key, value).let {
                if (!it) "findClass $value -> write false" else ""
            } else ""
            LogUtils.d(tag, "searchDexkit", msg)
        }
        return msg
    }
}