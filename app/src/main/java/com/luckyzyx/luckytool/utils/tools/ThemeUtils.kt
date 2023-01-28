@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import android.content.Context
import android.content.res.Configuration
import com.google.android.material.color.DynamicColors

class ThemeUtils(context: Context) {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()
    private val useDynamicColor = context.getBoolean(SettingsPrefs,"use_dynamic_color",false)
    private val followSystem = context.getBoolean(SettingsPrefs,"theme_follow_system",true)

    fun isDynamicColor() : Boolean{
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem() : Boolean{
        return supportDynamicColor && followSystem
    }

    fun isNightMode(configuration: Configuration): Boolean {
        return (configuration.uiMode and 32) > 0
    }
}