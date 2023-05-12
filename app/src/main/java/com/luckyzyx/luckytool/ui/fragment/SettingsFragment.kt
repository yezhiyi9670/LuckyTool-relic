package com.luckyzyx.luckytool.ui.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.ArraySet
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.Base64CodeUtils
import com.luckyzyx.luckytool.utils.DonateData
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.OtherPrefs
import com.luckyzyx.luckytool.utils.SettingsPrefs
import com.luckyzyx.luckytool.utils.backupAllPrefs
import com.luckyzyx.luckytool.utils.base64Decode
import com.luckyzyx.luckytool.utils.base64Encode
import com.luckyzyx.luckytool.utils.clearAllPrefs
import com.luckyzyx.luckytool.utils.formatDate
import com.luckyzyx.luckytool.utils.isZh
import com.luckyzyx.luckytool.utils.navigate
import com.luckyzyx.luckytool.utils.putBoolean
import com.luckyzyx.luckytool.utils.putInt
import com.luckyzyx.luckytool.utils.putString
import com.luckyzyx.luckytool.utils.putStringSet
import com.luckyzyx.luckytool.utils.readFromUri
import com.luckyzyx.luckytool.utils.setComponentDisabled
import com.luckyzyx.luckytool.utils.toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.system.exitProcess

class SettingsFragment : ModulePreferenceFragment() {
    private val backupData =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) {
            if (it != null) {
                writeBackupData(requireActivity(), it)
            }
        }
    private val restoreData =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                writeRestoreData(requireActivity(), readFromUri(requireActivity(), it))
            }
        }

    private fun writeBackupData(context: Context, uri: Uri) {
        val json = JSONObject()
        val dataMapList = context.backupAllPrefs(ModulePrefs, SettingsPrefs, OtherPrefs)
        dataMapList?.keys?.forEach { prefs ->
            val jsons = JSONObject()
            val data = dataMapList[prefs]
            data?.keys?.forEach { key ->
                data[key].apply {
                    if (this?.javaClass?.simpleName == "HashSet") {
                        val arr = JSONArray()
                        val value = (this as HashSet<*>).toTypedArray()
                        for (i in value.indices) {
                            arr.put(value[i])
                        }
                        jsons.put(key, arr)
                    } else {
                        jsons.put(key, this)
                    }
                }
            }
            json.put(prefs, jsons)
        }
        val str = base64Encode(json.toString())
        try {
            context.contentResolver.openFileDescriptor(uri, "w")?.use { its ->
                FileOutputStream(its.fileDescriptor).use {
                    it.write(str.toByteArray())
                }
            }
            context.toast(getString(R.string.data_backup_complete))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            context.toast(getString(R.string.data_backup_error))
        } catch (e: IOException) {
            e.printStackTrace()
            context.toast(getString(R.string.data_backup_error))
        }
    }

    private fun writeRestoreData(context: Context, data: String) {
        val json = JSONObject(base64Decode(data))
        if (json.length() <= 0) return
        json.keys().forEach { prefs ->
            val prefsDatas = json.getJSONObject(prefs)
            if (prefsDatas.length() > 0) {
                prefsDatas.keys().forEach { key ->
                    val value = prefsDatas.get(key)
                    when (value.javaClass.simpleName) {
                        "Boolean" -> context.putBoolean(prefs, key, value as Boolean)
                        "Integer" -> context.putInt(prefs, key, value as Int)
                        "JSONArray" -> {
                            val set = ArraySet<String>()
                            val list = value as JSONArray
                            for (i in 0 until list.length()) {
                                set.add(list[i] as String)
                            }
                            context.putStringSet(prefs, key, set)
                        }

                        "String" -> context.putString(prefs, key, value as String)
                        else -> context.toast("Error: $key")
                    }
                }
            }
        }
        context.toast(getString(R.string.data_restore_complete))
        (activity as MainActivity).restart()
    }

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SettingsPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(context).apply {
                    setTitle(R.string.theme_title)
                    setSummary(R.string.theme_title_summary)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    key = "use_dynamic_color"
                    setDefaultValue(false)
                    setTitle(R.string.use_dynamic_color)
                    setSummary(R.string.use_dynamic_color_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                }
            )
            addPreference(
                DropDownPreference(context).apply {
                    key = "dark_theme"
                    title = getString(R.string.dark_theme)
                    summary = "%s"
                    entries = resources.getStringArray(R.array.dark_theme)
                    entryValues = resources.getStringArray(R.array.dark_theme_value)
                    setDefaultValue("MODE_NIGHT_FOLLOW_SYSTEM")
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.other_settings)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    key = "auto_check_update"
                    title = getString(R.string.auto_check_update)
                    summary = getString(R.string.auto_check_update_summary)
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    key = "tile_auto_start"
                    title = getString(R.string.tile_auto_start)
                    summary = getString(R.string.tile_auto_start_summary)
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui")
                            .put("tile_auto_start", newValue)
                        true
                    }
                }
            )
            addPreference(
                DropDownPreference(context).apply {
                    title = getString(R.string.switch_autostart_function_caller)
                    summary =
                        getString(R.string.common_words_current_mode) + ": %s\n\n" + getString(R.string.switch_autostart_function_caller_summary)
                    key = "switch_autostart_function_caller"
                    entries =
                        resources.getStringArray(R.array.switch_autostart_function_caller_entries)
                    entryValues = arrayOf("0", "1")
                    setDefaultValue("0")
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui")
                            .put("switch_autostart_function_caller", newValue)
                        true
                    }
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    key = "hide_function_page_icon"
                    title = getString(R.string.hide_function_page_icon)
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    key = "hide_desktop_module_icon"
                    setDefaultValue(false)
                    title = getString(R.string.hide_desktop_module_icon)
                    summary = getString(R.string.hide_desktop_module_icon_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.setComponentDisabled(
                            ComponentName(
                                context.packageName, "${context.packageName}.Hide"
                            ), newValue as Boolean
                        )
                        true
                    }
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.backup_restore_clear)
                    key = "backup_restore_clear"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.backup_data)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        val fileName =
                            "LuckyTool_" + formatDate("yyyyMMdd_HHmmss") + "_backup.json"
                        backupData.launch(fileName)
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.restore_data)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        restoreData.launch("application/json")
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.clear_all_data)
                    summary = getString(R.string.clear_all_data_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        MaterialAlertDialogBuilder(context).apply {
                            setMessage(getString(R.string.clear_all_data_message))
                            setPositiveButton(android.R.string.ok) { _, _ ->
                                context.clearAllPrefs(ModulePrefs, SettingsPrefs, OtherPrefs)
                                exitProcess(0)
                            }
                            setNeutralButton(android.R.string.cancel, null)
                            show()
                        }
                        true
                    }
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    setTitle(R.string.about_title)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.donate)
                    summary = getString(R.string.donate_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        val donateList = arrayListOf<CharSequence>(
                            getString(R.string.qq),
                            getString(R.string.wechat),
                            getString(R.string.alipay),
                            getString(R.string.donation_list)
                        )
                        if (!isZh(context)) {
                            donateList.add(3, getString(R.string.patreon))
                            donateList.add(4, getString(R.string.paypal))
                        }
                        MaterialAlertDialogBuilder(context).apply {
                            setItems(donateList.toTypedArray()) { _, which ->
                                when (which) {
                                    0 -> DonateData(context).showQRCode(Base64CodeUtils.qqCode)
                                    1 -> DonateData(context).showQRCode(Base64CodeUtils.wechatCode)
                                    2 -> DonateData(context).showQRCode(Base64CodeUtils.alipayCode)
                                    3 -> if (isZh(context)) DonateData(context).showDonateList()
                                    else startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.patreon.com/LuckyTool")
                                        )
                                    )

                                    4 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://paypal.me/luckyzyx")
                                        )
                                    )

                                    5 -> DonateData(context).showDonateList()
                                }
                            }
                        }.show()
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.feedback_download)
                    summary = getString(R.string.feedback_download_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        val updatelist = arrayOf(
                            getString(R.string.coolmarket),
                            getString(R.string.module_doc),
                            getString(R.string.qq_channel),
                            getString(R.string.telegram_channel),
                            getString(R.string.telegram_group),
                            getString(R.string.lsposed_repo)
                        )
                        MaterialAlertDialogBuilder(context)
                            .setItems(updatelist) { _, which ->
                                when (which) {
                                    0 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("coolmarket://u/1930284")
                                        )
                                    )

                                    1 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://luckyzyx.github.io/LuckyTool_Doc/")
                                        )
                                    )

                                    2 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://pd.qq.com/s/ahjm4zyxb")
                                        )
                                    )

                                    3 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://t.me/LuckyTool")
                                        )
                                    )

                                    4 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://t.me/+F42pfv-c0h4zNDc9")
                                        )
                                    )

                                    5 -> startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://modules.lsposed.org/module/com.luckyzyx.luckytool")
                                        )
                                    )
                                }
                            }.show()
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.participate_translation)
                    summary = getString(R.string.participate_translation_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://crwd.in/luckytool")
                            )
                        )
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    setTitle(R.string.open_source)
                    setSummary(R.string.open_source_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        navigate(R.id.action_nav_setting_to_sourceFragment, title)
                        true
                    }
                }
            )
        }
    }
}

class SourceFragment : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(context).apply {
                    setTitle(R.string.open_source)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "Xposed"
                    summary = "rovo89 , Apache License 2.0"
                    isIconSpaceReserved = false
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rovo89/Xposed"))
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "LSPosed"
                    summary = "LSPosed , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/LSPosed/LSPosed"))
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "YukiHookAPI"
                    summary = "fankes , MIT License"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/fankes/YukiHookAPI")
                    )
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "ColorOSNotifyIcon"
                    summary = "fankes , AGPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/fankes/ColorOSNotifyIcon")
                    )
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "ColorOSTool"
                    summary = "Oosl , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Oosl/ColorOSTool"))
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "WooBoxForColorOS"
                    summary = "Simplicity-Team , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS")
                    )
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "CorePatch"
                    summary = "LSPosed , GPL-2.0 license"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/LSPosed/CorePatch")
                    )
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "DisableFlagSecure"
                    summary = "LSPosed , GPL-3.0 license"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/LSPosed/DisableFlagSecure")
                    )
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "FivegTile"
                    summary = "libxzr , MIT license"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/libxzr/FivegTile")
                    )
                }
            )
            addPreference(
                Preference(context).apply {
                    title = "WooBoxForMIUI"
                    summary = "LittleTurtle2333 , GPL-3.0 license"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Simplicity-Team/WooBoxForMIUI")
                    )
                }
            )
        }
    }
}