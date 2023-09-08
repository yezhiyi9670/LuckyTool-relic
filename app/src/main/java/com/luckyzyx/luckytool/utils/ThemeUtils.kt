@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors

object ThemeUtils {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()

    fun isDynamicColor(context: Context): Boolean {
        val useDynamicColor = context.getBoolean(SettingsPrefs, "use_dynamic_color", false)
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem(context: Context): Boolean {
        val followSystem = context.getBoolean(SettingsPrefs, "theme_follow_system", true)
        return supportDynamicColor && followSystem
    }

    /**
     * 是否为夜间模式
     */
    val Context.isNightMode get() = isNightMode(resources.configuration)

    /**
     * 是否为夜间模式
     * @param configuration Configuration
     * @return Boolean
     */
    fun isNightMode(configuration: Configuration): Boolean {
        return (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
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