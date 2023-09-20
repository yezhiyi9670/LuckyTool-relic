package com.luckyzyx.luckytool.hook.dexkit

import android.content.Context
import com.luckyzyx.luckytool.hook.scope.gallery.EnableWatermarkEditing
import com.luckyzyx.luckytool.hook.scope.gallery.ReplaceOnePlusModelWaterMark
import com.luckyzyx.luckytool.utils.DexkitUtils.printValue
import com.luckyzyx.luckytool.utils.arraySummaryLine

object OplusGallery {

    fun get(context: Context, appPath: String): String {
        return arraySummaryLine(
            EnableWatermarkEditing.let {
                it.searchDexkit(appPath).printValue(context, "EnableWatermarkEditing", it.key)
            },
            ReplaceOnePlusModelWaterMark.let {
                it.searchDexkit(appPath).printValue(context, "ReplaceOnePlusModelWaterMark", it.key)
            }
        )
    }
}