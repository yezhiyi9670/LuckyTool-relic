package com.luckyzyx.luckytool.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.util.ArrayMap
import androidx.core.graphics.drawable.toBitmap
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.ShortcutActivity

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ShortcutUtils(val context: Context) {

    /**
     * 获取内置快捷方式支持列表
     * @receiver Context
     * @return ArrayList<String>
     */
    fun getShortcutList(): ArrayMap<String, String> {
        val existOplusGame =
            context.checkPackName("com.oplus.games") && context.checkResolveActivity(
                Intent().setClassName(
                    "com.oplus.games",
                    "business.compact.activity.GameBoxCoverActivity"
                )
            )
        return ArrayMap<String, String>().apply {
            put("module_shortcut_status_lsposed", "LSPosed")
            if (existOplusGame) put(
                "module_shortcut_status_oplusgames",
                context.getAppLabel("com.oplus.games").toString()
            )
            put("module_shortcut_status_chargingtest", context.getString(R.string.charging_test))
            put(
                "module_shortcut_status_processmanager",
                context.getString(R.string.process_manager)
            )
            put(
                "module_shortcut_status_performance",
                context.getString(R.string.high_performance_mode)
            )
        }
    }

    fun getShortcutEnabledList(): ArrayList<ShortcutInfo> {
        val enabled = ArrayList<ShortcutInfo>()
        val list = getShortcutList()
        list.keys.forEach {
            if (context.getBoolean(SettingsPrefs, it, false)) {
                var icon: Icon? = null
                var intent: Intent? = null
                when (it) {
                    "module_shortcut_status_lsposed" -> {
                        icon = Icon.createWithResource(
                            context.packageName,
                            android.R.mipmap.sym_def_app_icon
                        )
                        intent = Intent(Intent.ACTION_VIEW).apply {
                            putExtra("Shortcut", it)
                            setClassName(context.packageName, ShortcutActivity::class.java.name)
                        }
                    }

                    "module_shortcut_status_oplusgames" -> {
                        icon = context.getAppIcon("com.oplus.games").let { drawable ->
                            if (drawable == null) Icon.createWithResource(
                                context.packageName, R.mipmap.oplusgames_icon
                            ) else Icon.createWithBitmap(drawable.toBitmap())
                        }
                        intent = Intent(Intent.ACTION_VIEW).apply {
                            putExtra("Shortcut", it)
                            setClassName(
                                "com.oplus.games",
                                "business.compact.activity.GameBoxCoverActivity"
                            )
                        }
                    }

                    "module_shortcut_status_chargingtest" -> {
                        icon = Icon.createWithResource(
                            context.packageName,
                            R.drawable.ic_baseline_charging_station_24
                        )
                        intent = Intent(Intent.ACTION_VIEW).apply {
                            putExtra("Shortcut", it)
                            setClassName(context.packageName, ShortcutActivity::class.java.name)
                        }
                    }

                    "module_shortcut_status_processmanager" -> {
                        icon = Icon.createWithResource(
                            context.packageName,
                            android.R.mipmap.sym_def_app_icon
                        )
                        intent = Intent(Intent.ACTION_VIEW).apply {
                            putExtra("Shortcut", it)
                            setClassName(context.packageName, ShortcutActivity::class.java.name)
                        }
                    }

                    "module_shortcut_status_performance" -> {
                        icon = Icon.createWithResource(
                            context.packageName,
                            R.drawable.baseline_device_thermostat_24
                        )
                        intent = Intent(Intent.ACTION_VIEW).apply {
                            putExtra("Shortcut", it)
                            setClassName(context.packageName, ShortcutActivity::class.java.name)
                        }
                    }
                }
                enabled.add(createShortcutInfo(it, list[it].toString(), icon, intent))
            }
        }
        return enabled
    }

    /**
     * 设置快捷方式启用状态
     * @param key String
     * @param status Boolean
     */
    fun setShortcutStatus(key: String, status: Boolean) {
        context.putBoolean(SettingsPrefs, key, status)
        if (!status) {
            removeDynamicShortcuts(arrayListOf(key))
        }
    }

    /**
     * 获取模块图标状态
     * @receiver Context
     * @return Boolean
     */
    fun getIconStatus(): Boolean {
        return when (context.getComponentEnabled(
            ComponentName(
                context.packageName,
                "${context.packageName}.Hide"
            )
        )) {
            0 -> true
            1 -> true
            else -> false
        }
    }

    /**
     * 创建快捷方式信息
     * @receiver Context
     * @param id String
     * @param label String
     * @param icon Icon
     * @param intent Intent
     * @return ShortcutInfo
     */
    fun createShortcutInfo(
        id: String,
        label: String,
        icon: Icon?,
        intent: Intent?
    ): ShortcutInfo {
        return ShortcutInfo.Builder(context, id).apply {
            setShortLabel(label)
            setIcon(icon)
            intent?.let { setIntent(it) }
        }.build()
    }

    /**
     * 设置动态快捷方式
     * @return Any
     */
    fun setDynamicShortcuts() = safeOf({
        context.toast("Set Dynamic Shortcuts Error!")
    }) {
        val shortcutManager =
            context.getSystemService(ShortcutManager::class.java) as ShortcutManager
        shortcutManager.dynamicShortcuts = getShortcutEnabledList()
    }

    /**
     * 设置动态快捷方式
     * @receiver Context
     * @param list Array<out ShortcutInfo>
     */
    fun setDynamicShortcuts(list: ArrayList<ShortcutInfo>) = safeOf({
        context.toast("Set Dynamic Shortcuts Error!")
    }) {
        val shortcutManager =
            context.getSystemService(ShortcutManager::class.java) as ShortcutManager
        shortcutManager.dynamicShortcuts = list
    }

    /**
     * 根据ID移除快捷方式
     * @receiver Context
     * @param list Array<out String>
     */
    fun removeDynamicShortcuts(list: ArrayList<String>) {
        val shortcutManager =
            context.getSystemService(ShortcutManager::class.java) as ShortcutManager
        shortcutManager.removeDynamicShortcuts(list)
    }
}