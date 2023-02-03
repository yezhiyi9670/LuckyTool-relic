@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors

class ThemeUtils(context: Context) {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()
    private val useDynamicColor = context.getBoolean(ModulePrefs, "use_dynamic_color", false)
    private val followSystem = context.getBoolean(ModulePrefs, "theme_follow_system", true)

    fun isDynamicColor(): Boolean {
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem(): Boolean {
        return supportDynamicColor && followSystem
    }

    fun Context.isNightMode(): Boolean {
        return (resources.configuration.uiMode and 32) > 0
    }

    /**
     * 初始化设置主题模式
     * @param string String? prefs参数
     */
    fun initTheme(string: String?) {
        when (string) {
            "MODE_NIGHT_FOLLOW_SYSTEM" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "MODE_NIGHT_NO" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "MODE_NIGHT_YES" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}