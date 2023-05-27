@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors

class ThemeUtils(context: Context) {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()
    private val useDynamicColor = context.getBoolean(SettingsPrefs, "use_dynamic_color", false)
    private val followSystem = context.getBoolean(SettingsPrefs, "theme_follow_system", true)

    fun isDynamicColor(): Boolean {
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem(): Boolean {
        return supportDynamicColor && followSystem
    }

    /**
     * 是否为夜间模式
     * @param context Context
     * @return Boolean
     */
    fun isNightMode(context: Context): Boolean {
        return (context.resources.configuration.uiMode and 32) > 0
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