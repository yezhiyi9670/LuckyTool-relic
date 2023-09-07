package com.luckyzyx.luckytool.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreference
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withDefault
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.A14
import com.luckyzyx.luckytool.utils.AppAnalyticsUtils.ckqcbss
import com.luckyzyx.luckytool.utils.FileUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.ThemeUtils
import com.luckyzyx.luckytool.utils.arraySummaryDot
import com.luckyzyx.luckytool.utils.arraySummaryLine
import com.luckyzyx.luckytool.utils.checkPackName
import com.luckyzyx.luckytool.utils.checkResolveActivity
import com.luckyzyx.luckytool.utils.dialogCentered
import com.luckyzyx.luckytool.utils.formatDate
import com.luckyzyx.luckytool.utils.getBoolean
import com.luckyzyx.luckytool.utils.getOSVersionCode
import com.luckyzyx.luckytool.utils.getString
import com.luckyzyx.luckytool.utils.isZh
import com.luckyzyx.luckytool.utils.jumpBattery
import com.luckyzyx.luckytool.utils.jumpGesture
import com.luckyzyx.luckytool.utils.jumpOTA
import com.luckyzyx.luckytool.utils.jumpPictorial
import com.luckyzyx.luckytool.utils.navigatePage
import com.luckyzyx.luckytool.utils.openApp
import com.luckyzyx.luckytool.utils.putString
import com.luckyzyx.luckytool.utils.replaceBlankLine
import com.luckyzyx.luckytool.utils.restartScopes
import com.luckyzyx.luckytool.utils.toast

class Android : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(Preference(context).apply {
                title = getString(R.string.ColorOSCorePatchTip)
                key = "ColorOSCorePatchTip"
                isIconSpaceReserved = false
            })
            addPreference(PreferenceCategory(context).apply {
                setTitle(R.string.corepatch)
                setSummary(R.string.corepatch_summary)
                key = "CorePatch"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                setTitle(R.string.downgr)
                setSummary(R.string.downgr_summary)
                key = "downgrade"
                setDefaultValue(true)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                setTitle(R.string.authcreak)
                setSummary(R.string.authcreak_summary)
                key = "authcreak"
                setDefaultValue(true)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                setTitle(R.string.digestCreak)
                setSummary(R.string.digestCreak_summary)
                key = "digestCreak"
                setDefaultValue(true)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                setTitle(R.string.UsePreSig)
                setSummary(R.string.UsePreSig_summary)
                key = "UsePreSig"
                setDefaultValue(true)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    if (newValue == true) {
                        MaterialAlertDialogBuilder(context, dialogCentered).apply {
                            setMessage(R.string.usepresig_warn)
                            setPositiveButton(android.R.string.ok, null)
                            show()
                        }
                    }
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                setTitle(R.string.enhancedMode)
                setSummary(R.string.enhancedMode_summary)
                key = "enhancedMode"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }
}

class StatusBar : ModulePreferenceFragment() {
    private val scopes =
        arrayOf("com.android.systemui", "com.oplus.battery", "com.coloros.phonemanager")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarClock)
                summary = arraySummaryDot(
                    getString(R.string.statusbar_clock_show_second),
                    getString(R.string.statusbar_clock_show_doublerow),
                    getString(
                        R.string.statusbar_clock_doublerow_fontsize
                    )
                )
                key = "StatusBarClock"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarClock, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarNetWorkSpeed)
                summary = arraySummaryDot(
                    getString(R.string.enable_double_row_network_speed),
                    getString(R.string.set_network_speed)
                )
                key = "StatusBarNetWorkSpeed"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarNetWorkSpeed, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarNotice)
                summary = arraySummaryDot(
                    getString(R.string.RemoveStatusBarNotifications),
                    getString(R.string.remove_notification_manager_limit)
                )
                key = "StatusBarNotice"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarNotice, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarIcon)
                summary = arraySummaryDot(
                    getString(R.string.remove_mobile_data_inout),
                    getString(R.string.remove_green_dot_privacy_prompt)
                )
                key = "StatusBarIcon"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarIcon, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarControlCenter)
                summary = arraySummaryDot(
                    getString(R.string.control_center_clock_show_second),
                    getString(R.string.remove_control_center_clock_red_one)
                )
                key = "StatusBarControlCenter"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarControlCenter, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarTiles)
                summary = arraySummaryDot(
                    getString(R.string.long_press_wifi_tile_open_the_page),
                    getString(R.string.fix_tile_align_both_sides)
                )
                key = "StatusBarTiles"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarTiles, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarLayout)
                summary = arraySummaryDot(
                    getString(R.string.statusbar_layout_mode),
                    getString(R.string.statusbar_layout_compatible_mode)
                )
                key = "StatusBarLayout"
                isIconSpaceReserved = false
                isVisible = SDK >= A13
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarLayout, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.StatusBarBattery)
                summary = arraySummaryDot(
                    getString(R.string.remove_statusbar_battery_percent),
                    getString(R.string.use_user_typeface)
                )
                key = "StatusBarBattery"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBar_to_statusBarBattery, title)
                    true
                }
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.StatusbarEvents)
                key = "StatusbarEvents"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.statusbar_double_click_lock_screen)
                key = "statusbar_double_click_lock_screen"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.vibrate_when_opening_the_statusbar)
                key = "vibrate_when_opening_the_statusbar"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_scroll_to_top_white_list)
                summary = getString(R.string.remove_scroll_to_top_white_list_summary)
                key = "remove_scroll_to_top_white_list"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
        }
        requireActivity().ckqcbss()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarClock : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.statusbar_clock_mode)
                summary = "%s"
                key = "statusbar_clock_mode"
                entries = resources.getStringArray(R.array.statusbar_clock_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getString(ModulePrefs, "statusbar_clock_mode", "0") == "1") {
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_year)
                    key = "statusbar_clock_show_year"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_month)
                    key = "statusbar_clock_show_month"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_day)
                    key = "statusbar_clock_show_day"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_week)
                    key = "statusbar_clock_show_week"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_period)
                    key = "statusbar_clock_show_period"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_double_hour)
                    key = "statusbar_clock_show_double_hour"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_second)
                    key = "statusbar_clock_show_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_hide_spaces)
                    key = "statusbar_clock_hide_spaces"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_clock_show_doublerow)
                    key = "statusbar_clock_show_doublerow"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                })
                addPreference(DropDownPreference(context).apply {
                    title = getString(R.string.statusbar_clock_text_alignment)
                    summary = "%s"
                    key = "statusbar_clock_text_alignment"
                    entries =
                        resources.getStringArray(R.array.statusbar_clock_text_alignment_entries)
                    entryValues = arrayOf("left", "center", "right")
                    setDefaultValue("center")
                    isVisible =
                        context.getBoolean(ModulePrefs, "statusbar_clock_show_doublerow", false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.statusbar_clock_singlerow_fontsize)
                    summary = getString(R.string.statusbar_clock_fontsize_summary)
                    key = "statusbar_clock_singlerow_fontsize"
                    setDefaultValue(0)
                    max = 18
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.statusbar_clock_doublerow_fontsize)
                    summary = getString(R.string.statusbar_clock_fontsize_summary)
                    key = "statusbar_clock_doublerow_fontsize"
                    setDefaultValue(0)
                    max = 10
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
            }
            if (context.getString(ModulePrefs, "statusbar_clock_mode", "0") == "2") {
                addPreference(EditTextPreference(context).apply {
                    title = getString(R.string.statusbar_clock_custom_format)
                    dialogTitle = getString(R.string.statusbar_clock_custom_format)
                    summary = context.getString(
                        ModulePrefs, "statusbar_clock_custom_format", "HH:mm:ss"
                    )
                    dialogMessage = """
                            YYYY/MM/dd -> ${formatDate("YYYY/MM/dd")}
                            Y/M/d/E/a -> ${formatDate("Y/M/d/E/a")}
                            YY/YYYY -> ${formatDate("YY/YYYY")}
                            M/MM/MMM/MMMM/MMMMM -> ${formatDate("M/MM/MMM/MMMM/MMMMM")}
                            d/dd/ddd/dddd -> ${formatDate("d/dd/d号/dd号")}
                            E/EE/EEE/EEEE/EEEEE -> ${formatDate("E/EE/EEE/EEEE/EEEEE")}
                            h/H/k/K -> ${formatDate("h/H/k/K")}
                            HH:mm:ss -> ${formatDate("HH:mm:ss")}
                            m/mm/mmm/mmmm -> ${formatDate("m/mm/mmm/mmmm")}
                            s/ss/sss/ssss -> ${formatDate("s/ss/sss/ssss")}
                            z -> ${formatDate("z")}
                            N -> 初一
                            NN -> 二月初一
                            NNN -> 兔年二月初一
                            NNNN -> 癸卯兔年二月初一
                            FF -> 凌晨/上午/傍晚/晚上
                            GG -> 子时/丑时/寅时/卯时
                        """.trimIndent()
                    key = "statusbar_clock_custom_format"
                    setDefaultValue("HH:mm:ss")
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        (activity as MainActivity).restart()
                        true
                    }
                })
                addPreference(DropDownPreference(context).apply {
                    title = getString(R.string.statusbar_clock_text_alignment)
                    summary = "%s"
                    key = "statusbar_clock_text_alignment"
                    entries =
                        resources.getStringArray(R.array.statusbar_clock_text_alignment_entries)
                    entryValues = arrayOf("left", "center", "right")
                    setDefaultValue("center")
                    val row = context.getString(
                        ModulePrefs, "statusbar_clock_custom_format", "HH:mm:ss"
                    )?.takeIf { e -> e.isNotBlank() }?.split("\n")?.size ?: 2
                    isVisible = row >= 2
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.statusbar_clock_custom_fontsize)
                    summary = getString(R.string.statusbar_clock_fontsize_summary)
                    key = "statusbar_clock_custom_fontsize"
                    setDefaultValue(0)
                    max = 20
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
            }
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.use_user_typeface)
                key = "statusbar_clock_user_typeface"
                setDefaultValue(false)
                isVisible = context.getString(ModulePrefs, "statusbar_clock_mode", "0") != "0"
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarNetWorkSpeed : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.set_network_speed)
                key = "set_network_speed"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.statusbar_network_layout)
                summary = "%s"
                key = "statusbar_network_layout"
                entries = resources.getStringArray(R.array.statusbar_network_layout_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.use_user_typeface)
                key = "statusbar_network_user_typeface"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            if (context.getString(ModulePrefs, "statusbar_network_layout", "0") != "0") {
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_network_no_second)
                    key = "statusbar_network_no_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
                if (context.getString(ModulePrefs, "statusbar_network_layout", "0") == "1") {
                    addPreference(SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_network_no_space)
                        key = "statusbar_network_no_space"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            context.dataChannel("com.android.systemui").put(key, newValue)
                            true
                        }
                    })
                }
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.set_network_speed_font_size)
                    key = "set_network_speed_font_size"
                    setDefaultValue(7)
                    max = 8
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.set_network_speed_padding_bottom)
                    key = "set_network_speed_padding_bottom"
                    setDefaultValue(0)
                    max = 4
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
                if (context.getString(ModulePrefs, "statusbar_network_layout", "0") == "2") {
                    addPreference(SeekBarPreference(context).apply {
                        title = getString(R.string.set_network_speed_double_row_spacing)
                        key = "set_network_speed_double_row_spacing"
                        setDefaultValue(-1)
                        max = 6
                        min = -1
                        showSeekBarValue = true
                        updatesContinuously = false
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            context.dataChannel("com.android.systemui").put(key, newValue)
                            true
                        }
                    })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarNotifyRemoval : ModulePreferenceFragment() {
    private val scopes =
        arrayOf("com.android.systemui", "com.oplus.battery", "com.coloros.phonemanager")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_statusbar_top_notification)
                summary = getString(R.string.remove_statusbar_top_notification_summary)
                key = "remove_statusbar_top_notification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_vpn_active_notification)
                summary = getString(R.string.remove_vpn_active_notification_summary)
                key = "remove_vpn_active_notification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_statusbar_devmode)
                key = "remove_statusbar_devmode"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_charging_completed)
                key = "remove_charging_completed"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_flashlight_open_notification)
                key = "remove_flashlight_open_notification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_app_high_battery_consumption_warning)
                summary = getString(R.string.remove_app_high_battery_consumption_warning_summary)
                key = "remove_app_high_battery_consumption_warning"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_high_performance_mode_notifications)
                key = "remove_high_performance_mode_notifications"
                setDefaultValue(false)
                isIconSpaceReserved = false
                isVisible = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_do_not_disturb_mode_notification)
                key = "remove_do_not_disturb_mode_notification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_hotspot_power_consumption_notification)
                summary = getString(R.string.remove_hotspot_power_consumption_notification_summary)
                key = "remove_hotspot_power_consumption_notification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_smart_rapid_charging_notification)
                key = "remove_smart_rapid_charging_notification"
                setDefaultValue(false)
                isVisible = false
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_notifications_for_mute_notifications)
                key = "remove_notifications_for_mute_notifications"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_virus_risk_notification_in_phone_manager)
                key = "remove_virus_risk_notification_in_phone_manager"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_gt_mode_notification)
                key = "remove_gt_mode_notification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarNotify : ModulePreferenceFragment() {
    private val scopes = arrayOf(
        "com.android.systemui",
        "com.oplus.battery",
        "com.coloros.phonemanager",
        "com.oplus.notificationmanager"
    )

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(Preference(context).apply {
                title = getString(R.string.RemoveStatusBarNotifications)
                summary = arraySummaryDot(
                    getString(R.string.remove_statusbar_top_notification),
                    getString(R.string.remove_statusbar_devmode)
                )
                key = "RemoveStatusBarNotifications"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_statusBarNotice_to_statusBarNotifyRemoval, title)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.allow_long_press_notification_modifiable)
                key = "allow_long_press_notification_modifiable"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_notification_manager_limit)
                key = "remove_notification_manager_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_small_window_reply_whitelist)
                key = "remove_small_window_reply_whitelist"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "remove_small_window_reply_whitelist")) {
                addPreference(EditTextPreference(context).apply {
                    title = getString(R.string.set_small_window_reply_blacklist)
                    dialogTitle = title
                    summary = context.getString(
                        ModulePrefs, "set_small_window_reply_blacklist", "None"
                    )
                    if (summary.isNullOrBlank()) summary = "None"
                    dialogMessage = getString(R.string.set_small_window_reply_blacklist_message)
                    key = "set_small_window_reply_blacklist"
                    setDefaultValue("None")
                    isIconSpaceReserved = false
                    setOnBindEditTextListener {
                        it.setText((summary as String).replaceBlankLine)
                    }
                    setOnPreferenceChangeListener { _, newValue ->
                        val format = (newValue as String).replaceBlankLine
                        summary = format.ifBlank { "None" }
                        context.dataChannel("com.android.systemui").put(key, format)
                        true
                    }
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarIcon : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.StatusBarWIFIIcon)
                key = "StatusBarWIFIIcon"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_wifi_data_inout)
                key = "remove_wifi_data_inout"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.StatusBarMobileDataIcon)
                key = "StatusBarMobileDataIcon"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_mobile_data_inout)
                key = "remove_mobile_data_inout"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_mobile_data_type)
                key = "remove_mobile_data_type"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.hide_non_network_card_icon)
                key = "hide_non_network_card_icon"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.hide_inactive_signal_labels_gen2x2)
                key = "hide_inactive_signal_labels_gen2x2"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.hide_nosim_noservice)
                key = "hide_nosim_noservice"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.StatusBarBluetoothIcon)
                key = "StatusBarBluetoothIcon"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.hide_icon_when_bluetooth_not_connected)
                key = "hide_icon_when_bluetooth_not_connected"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.StatusBarOtherIcon)
                key = "StatusBarOtherIcon"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_high_performance_mode_icon)
                key = "remove_high_performance_mode_icon"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_statusbar_securepayment_icon)
                key = "remove_statusbar_securepayment_icon"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_green_dot_privacy_prompt)
                key = "remove_green_dot_privacy_prompt"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_green_capsule_prompt)
                summary = getString(R.string.remove_green_capsule_prompt_summary)
                key = "remove_green_capsule_prompt"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarControlCenter : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.ControlCenter_Clock_Related)
                key = "ControlCenter_Clock_Related"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.control_center_clock_show_second)
                key = "control_center_clock_show_second"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.statusbar_control_center_clock_red_one_mode)
                summary = "%s"
                key = "statusbar_control_center_clock_red_one_mode"
                entries =
                    resources.getStringArray(R.array.statusbar_control_center_clock_red_one_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.statusbar_control_center_clock_colon_style)
                summary = "%s"
                key = "statusbar_control_center_clock_colon_style"
                entries =
                    resources.getStringArray(R.array.statusbar_control_center_clock_colon_style_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                isVisible = SDK >= A13
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_control_center_date_comma)
                key = "remove_control_center_date_comma"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.statusbar_control_center_date_show_lunar)
                key = "statusbar_control_center_date_show_lunar"
                setDefaultValue(false)
                isVisible = isZh(context)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    (activity as MainActivity).restart()
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.statusbar_control_center_date_fix_width)
                key = "statusbar_control_center_date_fix_width"
                setDefaultValue(false)
                isVisible = SDK >= A13 && isZh(context)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            if (context.getBoolean(
                    ModulePrefs, "statusbar_control_center_date_show_lunar", false
                )
            ) {
                addPreference(DropDownPreference(context).apply {
                    title = getString(R.string.statusbar_control_center_date_fix_lunar_horizontal)
                    summary = "%s"
                    key = "statusbar_control_center_date_fix_lunar_horizontal"
                    entries =
                        resources.getStringArray(R.array.statusbar_control_center_date_fix_lunar_horizontal_entries)
                    entryValues = arrayOf("0", "1", "2")
                    setDefaultValue("0")
                    isVisible = SDK >= A13 && isZh(context)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        true
                    }
                })
            }

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.ControlCenterNotificationCenter)
                key = "ControlCenterNotificationCenter"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_notification_align_both_sides)
                key = "enable_notification_align_both_sides"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_notification_importance_classification)
                key = "enable_notification_importance_classification"
                setDefaultValue(false)
                isVisible = SDK < A14
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.ControlCenter_UI_Related)
                key = "ControlCenter_UI_Related"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_display_media_player)
                key = "force_display_media_player"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "force_display_media_player", false)) {
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.force_enable_media_toggle_button)
                    key = "force_enable_media_toggle_button"
                    setDefaultValue(false)
                    isVisible = SDK >= A13
                    isIconSpaceReserved = false
                })
            }
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_auto_brightness_button_mode)
                summary = "%s"
                key = "set_auto_brightness_button_mode"
                entries =
                    resources.getStringArray(R.array.statusbar_control_center_auto_brightness_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_control_center_user_switcher)
                key = "remove_control_center_user_switcher"
                setDefaultValue(false)
                isVisible = SDK < A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_control_center_mydevice)
                key = "remove_control_center_mydevice"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_control_center_search_button_mode)
                summary = "%s"
                key = "set_control_center_search_button_mode"
                entries =
                    resources.getStringArray(R.array.set_control_center_search_button_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.remove_control_center_networkwarn)
                summary = "%s\n" + getString(R.string.remove_control_center_networkwarn_summary)
                key = "remove_control_center_networkwarn"
                entries =
                    resources.getStringArray(R.array.statusbar_control_center_networkwarn_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SeekBarPreference(context).apply {
                title = getString(R.string.custom_control_center_background_transparency)
                summary = getString(R.string.force_enable_systemui_blur_feature_tips)
                key = "custom_control_center_background_transparency"
                setDefaultValue(-1)
                max = 10
                min = -1
                showSeekBarValue = true
                updatesContinuously = false
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarTiles : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.TileLongClickEvent)
                key = "TileLongClickEvent"
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.restore_some_tile_long_press_event)
                summary = getString(R.string.restore_some_tile_long_press_event_summary)
                key = "restore_some_tile_long_press_event"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.TileLayoutRelated)
                key = "TileLayoutRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.fix_tile_align_both_sides)
                summary = getString(R.string.fix_tile_align_both_sides_summary)
                key = "fix_tile_align_both_sides"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.restore_page_layout_row_count_for_edit_tiles)
                key = "restore_page_layout_row_count_for_edit_tiles"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.control_center_tile_enable)
                key = "control_center_tile_enable"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "control_center_tile_enable", false)) {
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_unexpanded_columns_vertical)
                    key = "tile_unexpanded_columns_vertical"
                    setDefaultValue(6)
                    max = 6
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_unexpanded_columns_horizontal)
                    key = "tile_unexpanded_columns_horizontal"
                    setDefaultValue(6)
                    max = 8
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_columns_vertical)
                    key = "tile_expanded_columns_vertical"
                    setDefaultValue(4)
                    max = 7
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_columns_horizontal)
                    key = "tile_expanded_columns_horizontal"
                    setDefaultValue(6)
                    max = 9
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_unexpanded_columns_vertical)
                    key = "tile_unexpanded_columns_vertical_c13"
                    setDefaultValue(5)
                    max = 6
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_rows_vertical)
                    key = "tile_expanded_rows_vertical_c13"
                    setDefaultValue(3)
                    max = 6
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_columns_vertical)
                    key = "tile_expanded_columns_vertical_c13"
                    setDefaultValue(4)
                    max = 7
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.tile_columns_horizontal_c13)
                    key = "tile_columns_horizontal_c13"
                    setDefaultValue(5)
                    max = 6
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarLayout : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.statusbar_layout_mode)
                summary = "%s"
                key = "statusbar_layout_mode"
                entries = resources.getStringArray(R.array.statusbar_layout_mode_entries)
                entryValues = arrayOf("0", "1")
                setDefaultValue("0")
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.statusbar_layout_compatible_mode)
                key = "statusbar_layout_compatible_mode"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(
                    ModulePrefs, "statusbar_layout_compatible_mode", false
                )
            ) {
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.statusbar_layout_left_margin)
                    summary = getString(R.string.statusbar_layout_margin_tip)
                    key = "statusbar_layout_left_margin"
                    setDefaultValue(0)
                    max = 150
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.statusbar_layout_right_margin)
                    summary = getString(R.string.statusbar_layout_margin_tip)
                    key = "statusbar_layout_right_margin"
                    setDefaultValue(0)
                    max = 150
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class StatusBarBattery : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_statusbar_battery_percent)
                key = "remove_statusbar_battery_percent"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.use_user_typeface)
                key = "statusbar_power_user_typeface"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "statusbar_power_user_typeface", false)) {
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.statusbar_power_font_size)
                    summary = getString(R.string.statusbar_clock_fontsize_summary)
                    key = "statusbar_power_font_size"
                    setDefaultValue(0)
                    max = 10
                    min = 0
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
            }
            if (SDK >= A12) {
                addPreference(PreferenceCategory(context).apply {
                    title = getString(R.string.StatusBarBatteryNotify)
                    key = "StatusBarBatteryNotify"
                    isIconSpaceReserved = false
                })
                addPreference(DropDownPreference(context).apply {
                    title = getString(R.string.battery_information_display_mode)
                    summary = "%s\n" + getString(R.string.battery_information_display_mode_summary)
                    key = "battery_information_display_mode"
                    entries =
                        resources.getStringArray(R.array.statusbar_battery_information_notify_entries)
                    entryValues = arrayOf("0", "1", "2")
                    setDefaultValue("0")
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        context.dataChannel("com.android.systemui").put(key, newValue)
                        (activity as MainActivity).restart()
                        true
                    }
                })
                if (context.getString(
                        ModulePrefs, "battery_information_display_mode", "0"
                    ) != "0"
                ) {
                    addPreference(SwitchPreference(context).apply {
                        title = getString(R.string.battery_information_show_charge)
                        summary = getString(R.string.battery_information_show_charge_summary)
                        key = "battery_information_show_charge_info"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            context.dataChannel("com.android.systemui").put(key, newValue)
                            true
                        }
                    })
                    addPreference(SwitchPreference(context).apply {
                        title = getString(R.string.battery_information_show_dual_voltage)
                        key = "battery_information_show_dual_voltage"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            context.dataChannel("com.android.systemui").put(key, newValue)
                            true
                        }
                    })
                    addPreference(SwitchPreference(context).apply {
                        title = getString(R.string.battery_information_show_simple_mode)
                        key = "battery_information_show_simple_mode"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            context.dataChannel("com.android.systemui").put(key, newValue)
                            true
                        }
                    })
                    addPreference(SwitchPreference(context).apply {
                        title = getString(R.string.battery_information_show_update_time)
                        summary = getString(R.string.battery_information_show_update_time_summary)
                        key = "battery_information_show_update_time"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            context.dataChannel("com.android.systemui").put(key, newValue)
                            true
                        }
                    })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Launcher : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.coloros.alarmclock", "com.android.launcher")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.alarmclock_widget_redone_mode)
                summary = "%s"
                key = "alarmclock_widget_redone_mode"
                entries =
                    resources.getStringArray(R.array.statusbar_control_center_clock_red_one_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.coloros.alarmclock").put(key, newValue)
                    true
                }
            })
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.AppBadgeRelated)
                key = "AppBadgeRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_the_dot_after_app_update)
                key = "remove_the_dot_after_app_update"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            if (SDK >= A13) {
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.remove_app_shortcut_badge)
                    key = "remove_app_shortcut_badge"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.remove_app_work_badge)
                    key = "remove_app_work_badge"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.remove_app_clone_badge)
                    key = "remove_app_clone_badge"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
            }
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.FolderLayoutRelated)
                key = "FolderLayoutRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_folder_preview_background)
                key = "remove_folder_preview_background"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_folder_layout_adjustment)
                key = "enable_folder_layout_adjustment"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "enable_folder_layout_adjustment", false)) {
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.set_icon_columns_in_folder)
                    key = "set_icon_columns_in_folder"
                    setDefaultValue(3)
                    max = 7
                    min = 3
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
            }
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.PaginationComponentRelated)
                key = "PaginationComponentRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_pagination_component)
                key = "remove_pagination_component"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_folder_pagination_component)
                key = "remove_folder_pagination_component"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_pagination_component_sliding)
                key = "disable_pagination_component_sliding"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.RecentTaskListRelated)
                key = "RecentTaskListRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_stacked_task_layout)
                key = "enable_stacked_task_layout"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "enable_stacked_task_layout", false)) {
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.set_task_stacking_level)
                    key = "set_task_stacking_level"
                    setDefaultValue(7)
                    max = 10
                    min = 5
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.fix_current_task_to_the_top)
                    key = "fix_current_task_to_the_top"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                })
            }
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.long_press_app_icon_open_app_details)
                key = "long_press_app_icon_open_app_details"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_bottom_app_icon_of_recent_task_list)
                key = "remove_bottom_app_icon_of_recent_task_list"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_recent_task_list_clear_button)
                key = "remove_recent_task_list_clear_button"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.launcher_layout_related)
                key = "DesktopLayoutRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.launcher_layout_enable)
                summary = getString(R.string.launcher_layout_row_colume)
                key = "launcher_layout_enable"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "launcher_layout_enable", false)) {
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.launcher_layout_max_rows)
                    key = "launcher_layout_max_rows"
                    setDefaultValue(6)
                    max = 10
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
                addPreference(SeekBarPreference(context).apply {
                    title = getString(R.string.launcher_layout_max_columns)
                    key = "launcher_layout_max_columns"
                    setDefaultValue(4)
                    max = 8
                    min = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                })
            }
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.launcher_events)
                key = "LauncherEvents"
                isVisible = false
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Aod : ModulePreferenceFragment() {

    private val scopes = arrayOf("com.android.systemui", "com.oplus.aod", "com.oplus.uiengine")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.AodRelated)
                key = "AodRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_aod_music_whitelist)
                key = "remove_aod_music_whitelist"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_aod_notification_icon_whitelist)
                key = "remove_aod_notification_icon_whitelist"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_aod_style_mode)
                summary = "%s"
                key = "set_aod_style_mode"
                entries = resources.getStringArray(R.array.set_aod_style_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_enable_screen_off_music_support)
                summary = getString(R.string.force_enable_screen_off_music_support_summary)
                key = "force_enable_screen_off_music_support"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class LockScreen : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.LockScreenClockComponent)
                key = "LockScreenClockComponent"
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.lock_screen_clock_redone_mode)
                summary = "%s"
                key = "lock_screen_clock_redone_mode"
                entries =
                    resources.getStringArray(R.array.statusbar_control_center_clock_red_one_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.apply_lock_screen_dual_clock_redone)
                key = "apply_lock_screen_dual_clock_redone"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.set_lock_screen_centered)
                summary = getString(R.string.set_lock_screen_centered_summary)
                key = "set_lock_screen_centered"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.lock_screen_clock_use_user_typeface)
                key = "lock_screen_clock_use_user_typeface"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.LockScreenChargingComponent)
                key = "LockScreenChargingComponent"
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_lock_screen_warp_charging_style)
                summary = "%s"
                key = "set_lock_screen_warp_charging_style"
                entries =
                    resources.getStringArray(R.array.set_lock_screen_warp_charging_style_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isVisible = SDK == A13
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    (activity as MainActivity).restart()
                    true
                }
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_lock_screen_charging_text_logo_style)
                summary = "%s"
                key = "set_lock_screen_charging_text_logo_style"
                entries =
                    resources.getStringArray(R.array.set_lock_screen_charging_text_logo_style_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_lock_screen_charging_show_wattage)
                key = "force_lock_screen_charging_show_wattage"
                setDefaultValue(false)
                isVisible = SDK == A13
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.lock_screen_charging_use_user_typeface)
                key = "lock_screen_charging_use_user_typeface"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_full_screen_charging_animation_mode)
                summary = "%s\n" + getString(R.string.need_restart_scope)
                key = "set_full_screen_charging_animation_mode"
                entries =
                    resources.getStringArray(R.array.set_full_screen_charging_animation_mode_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isVisible = getOSVersionCode >= 27
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.LockScreenCarrier)
                key = "LockScreenCarrier"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_statusbar_carriers)
                key = "remove_statusbar_carriers"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.statusbar_carriers_use_user_typeface)
                key = "statusbar_carriers_use_user_typeface"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.LockScreenButton)
                key = "LockScreenButton"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_top_lock_screen_icon)
                key = "remove_top_lock_screen_icon"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_lock_screen_bottom_left_button)
                key = "remove_lock_screen_bottom_left_button"
                setDefaultValue(false)
                isVisible = SDK < A14
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    (activity as MainActivity).restart()
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.lock_screen_bottom_left_button_replace_with_flashlight)
                key = "lock_screen_bottom_left_button_replace_with_flashlight"
                setDefaultValue(false)
                isVisible = SDK < A14 && context.getBoolean(
                    ModulePrefs, "remove_lock_screen_bottom_left_button", false
                ) == false
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.lock_screen_switch_flashlight_auto_close_screen)
                key = "lock_screen_switch_flashlight_auto_close_screen"
                setDefaultValue(false)
                isVisible = SDK < A14 && context.getBoolean(
                    ModulePrefs, "remove_lock_screen_bottom_left_button", false
                ) == false
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_lock_screen_bottom_right_camera)
                key = "remove_lock_screen_bottom_right_camera"
                setDefaultValue(false)
                isVisible = SDK < A14
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_lock_screen_close_notification_button)
                key = "remove_lock_screen_close_notification_button"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_lock_screen_bottom_sos_button)
                summary = getString(R.string.remove_lock_screen_bottom_sos_button_summary)
                key = "remove_lock_screen_bottom_sos_button"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.LockScreenEvent)
                key = "LockScreenEvent"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_72hour_password_verification)
                summary = getString(R.string.remove_72hour_password_verification_summary)
                key = "remove_72hour_password_verification"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Screenshot : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.oplus.screenshot")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_system_screenshot_delay)
                summary = getString(R.string.remove_system_screenshot_delay_summary)
                key = "remove_system_screenshot_delay"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_screenshot_privacy_limit)
                summary = getString(R.string.remove_screenshot_privacy_limit_summary)
                key = "remove_screenshot_privacy_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_flag_secure)
                summary = getString(R.string.disable_flag_secure_summary)
                key = "disable_flag_secure"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_page_limit_for_long_screenshots)
                summary = getString(R.string.remove_page_limit_for_long_screenshots_summary)
                key = "remove_page_limit_for_long_screenshots"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_png_save_format)
                key = "enable_png_save_format"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Application : ModulePreferenceFragment() {

    private val scopes = arrayOf(
        "com.oplus.battery",
        "com.oplus.safecenter",
        "com.coloros.safecenter",
        "com.android.launcher",
        "com.oppo.launcher",
        "com.android.settings",
        "com.android.packageinstaller",
        "com.android.permissioncontroller"
    )

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.AppStartupRelated)
                key = "AppStartupRelated"
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_splash_screen)
                summary = arraySummaryLine(
                    getString(R.string.need_restart_system),
                    getString(R.string.disable_splash_screen_summary)
                )
                key = "disable_splash_screen"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.APPRelatedList)
                key = "APPRelatedList"
                isIconSpaceReserved = false
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.zoom_window_support_list)
                summary = getString(R.string.zoom_window_support_list_summary)
                key = "zoom_window_support_list"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_application_to_zoomWindowFragment, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.dark_mode_support_list)
                summary = getString(R.string.zoom_window_support_list_summary)
                key = "dark_mode_support_list"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_application_to_darkModeFragment, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.multi_app_custom_list)
                summary = getString(R.string.multi_app_custom_list_summary)
                key = "multi_app_custom_list"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_application_to_multiFragment, title)
                    true
                }
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.AppInstallationRelated)
                summary = getString(R.string.PackageInstaller_summary)
                key = "PackageInstaller"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.skip_apk_scan)
                summary = getString(R.string.skip_apk_scan_summary)
                key = "skip_apk_scan"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.allow_downgrade_install)
                summary = getString(R.string.allow_downgrade_install_summary)
                key = "allow_downgrade_install"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_install_ads)
                summary = getString(R.string.remove_install_ads_summary)
                key = "remove_install_ads"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.auto_click_install_button)
                key = "auto_click_install_button"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.auto_click_uninstall_button)
                key = "auto_click_uninstall_button"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.show_packagename_and_versioncode)
                summary = getString(R.string.show_packagename_and_versioncode_summary)
                key = "show_packagename_and_versioncode"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.replase_aosp_installer)
                summary = getString(R.string.replase_aosp_installer_summary)
                key = "replase_aosp_installer"
                setDefaultValue(false)
                isVisible = false
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_adb_install_confirm)
                summary = getString(R.string.remove_adb_install_confirm_summary)
                key = "remove_adb_install_confirm"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.ApplyOtherRestrictions)
                key = "ApplyOtherRestrictions"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.unlock_startup_limit)
                summary = getString(R.string.unlock_startup_limit_summary)
                key = "unlock_startup_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.unlock_task_locks)
                key = "unlock_task_locks"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.unlock_default_desktop_limit)
                key = "unlock_default_desktop_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_all_apps_support_split_screen)
                key = "force_all_apps_support_split_screen"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("android").put(key, newValue as Boolean)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_app_uninstall_button_blacklist)
                key = "remove_app_uninstall_button_blacklist"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.allow_locking_unlocking_of_excluded_activity)
                key = "allow_locking_unlocking_of_excluded_activity"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class DialogRelated : ModulePreferenceFragment() {
    private val scopes = arrayOf(
        "com.android.systemui", "com.oplus.exsystemservice", "com.coloros.securepay"
    )

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_duplicate_floating_window)
                summary = getString(R.string.disable_duplicate_floating_window_summary)
                key = "disable_duplicate_floating_window"
                setDefaultValue(false)
                isVisible = getOSVersionCode >= 26
                isIconSpaceReserved = false

            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_headphone_high_volume_warning)
                summary = getString(R.string.disable_headphone_high_volume_warning_summary)
                key = "disable_headphone_high_volume_warning"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_low_battery_dialog_warning)
                summary = getString(R.string.remove_low_battery_dialog_warning_summary)
                key = "remove_low_battery_dialog_warning"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_usb_connect_dialog)
                summary = getString(R.string.remove_usb_connect_dialog_summary)
                key = "remove_usb_connect_dialog"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_secure_pay_found_virus_dialog)
                key = "remove_secure_pay_found_virus_dialog"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_access_device_log_dialog)
                key = "remove_access_device_log_dialog"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_volume_bar_display_position)
                summary = "%s"
                key = "set_volume_bar_display_position"
                entries = resources.getStringArray(R.array.set_volume_bar_display_position_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SeekBarPreference(context).apply {
                title = getString(R.string.custom_volume_dialog_background_transparency)
                summary = getString(R.string.force_enable_systemui_blur_feature_tips)
                key = "custom_volume_dialog_background_transparency"
                setDefaultValue(-1)
                max = 10
                min = -1
                showSeekBarValue = true
                updatesContinuously = false
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    context.dataChannel("com.android.systemui").put(key, newValue)
                    true
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class FingerPrintRelated : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui")
    private val loadFPIcon = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val path = FileUtils.getDocumentPath(requireActivity(), it)
            requireActivity().putString(
                ModulePrefs, "replace_fingerprint_icon_path", path
            )
            findPreference<Preference>("replace_fingerprint_icon_path")?.summary = path
        }
    }

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.remove_fingerprint_icon_mode)
                summary = "%s"
                key = "remove_fingerprint_icon_mode"
                entries = resources.getStringArray(R.array.remove_fingerprint_icon_mode_entries)
                entryValues = arrayOf("0", "1", "2", "3")
                setDefaultValue("0")
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.replace_fingerprint_icon_switch)
                summary = getString(R.string.replace_fingerprint_icon_switch_summary)
                key = "replace_fingerprint_icon_switch"
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "replace_fingerprint_icon_switch", false)) {
                addPreference(Preference(context).apply {
                    title = getString(R.string.replace_fingerprint_icon_path)
                    key = "replace_fingerprint_icon_path"
                    summary = context.getString(
                        ModulePrefs, "replace_fingerprint_icon_path", "null"
                    )
                    isIconSpaceReserved = false
                    isCopyingEnabled = true
                    setOnPreferenceClickListener {
                        loadFPIcon.launch("image/*")
                        true
                    }
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Miscellaneous : ModulePreferenceFragment() {
    private val scopes = arrayOf(
        "com.android.systemui",
        "com.android.externalstorage",
        "com.oplus.exsystemservice",
        "com.coloros.securepay"
    )

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(Preference(context).apply {
                title = getString(R.string.FloatingWindowDialogRelated)
                summary = arraySummaryDot(
                    getString(R.string.remove_low_battery_dialog_warning_summary),
                    getString(R.string.disable_headphone_high_volume_warning)
                )
                key = "FloatingWindowDialogRelated"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_miscellaneous_to_dialogRelated, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.FingerPrintRelated)
                summary = arraySummaryDot(
                    getString(R.string.remove_fingerprint_icon),
                    getString(R.string.replace_fingerprint_icon_switch)
                )
                key = "FingerPrintRelated"
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_miscellaneous_to_fingerPrintRelated, title)
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.show_charging_ripple)
                summary = getString(R.string.show_charging_ripple_summary)
                key = "show_charging_ripple"
                setDefaultValue(false)
                isVisible = SDK >= A12
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_otg_auto_off)
                summary = getString(R.string.disable_otg_auto_off_summary)
                key = "disable_otg_auto_off"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_storage_limit)
                summary = getString(R.string.remove_storage_limit_summary)
                key = "remove_storage_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SeekBarPreference(context).apply {
                title = getString(R.string.media_volume_level)
                summary = getString(R.string.media_volume_level_summary)
                key = "media_volume_level"
                setDefaultValue(0)
                max = 50
                min = 0
                showSeekBarValue = true
                updatesContinuously = false
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.minimum_volume_level_can_be_zero)
                key = "minimum_volume_level_can_be_zero"
                setDefaultValue(false)
                isVisible = SDK >= A12
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.allow_untrusted_touch)
                key = "allow_untrusted_touch"
                setDefaultValue(false)
                isVisible = SDK >= A12
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_dynamic_refresh_rate)
                summary = getString(R.string.disable_dynamic_refresh_rate_summary)
                key = "disable_dynamic_refresh_rate"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_enable_systemui_blur_feature)
                key = "force_enable_systemui_blur_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Settings : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.settings")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.settings_lock_screen)
                key = "settings_status_bar"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_statusbar_clock_format)
                summary = getString(R.string.enable_statusbar_clock_format_summary)
                key = "enable_statusbar_clock_format"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.settings_lock_screen)
                key = "settings_lock_screen"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_show_never_timeout)
                summary = getString(R.string.enable_show_never_timeout_summary)
                key = "enable_show_never_timeout"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.settings_display)
                key = "settings_display"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_display_dc_backlight_mode)
                summary = getString(R.string.force_display_dc_backlight_mode_summary)
                key = "force_display_dc_backlight_mode"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_display_video_memc_frame_insertion)
                summary = getString(R.string.force_display_dc_backlight_mode_summary)
                key = "force_display_video_memc_frame_insertion"
                setDefaultValue(false)
                isVisible = false
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.video_frame_insertion_support_2K120)
                summary = getString(R.string.video_frame_insertion_support_2K120_summary)
                key = "video_frame_insertion_support_2K120"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_screen_color_temperature_rgb_palette)
                summary = arraySummaryLine(
                    getString(R.string.need_restart_system),
                    getString(R.string.enable_screen_color_temperature_rgb_palette_summary)
                )
                key = "enable_screen_color_temperature_rgb_palette"
                setDefaultValue(false)
                isVisible = getOSVersionCode >= 27
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.settings_other_preference)
                key = "settings_other_preference"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_top_account_display)
                key = "remove_top_account_display"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_dpi_restart_recovery)
                summary = getString(R.string.remove_dpi_restart_recovery_summary)
                key = "remove_dpi_restart_recovery"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_display_bottom_google_settings)
                key = "force_display_bottom_google_settings"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.disable_cn_special_edition_setting)
                key = "disable_cn_special_edition_setting"
                setDefaultValue(false)
                isVisible = isZh(context)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.force_display_process_management)
                key = "force_display_process_management"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_settings_bottom_laboratory)
                key = "remove_settings_bottom_laboratory"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(DropDownPreference(context).apply {
                title = getString(R.string.set_processor_click_page)
                summary = "%s"
                key = "set_processor_click_page"
                entries = resources.getStringArray(R.array.set_processor_click_page_entries)
                entryValues = arrayOf("0", "1", "2")
                setDefaultValue("0")
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.customize_device_sharing_page_parameters)
                key = "customize_device_sharing_page_parameters"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.AppDetailsRelated)
                key = "AppDetailsRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.show_package_name_in_app_details)
                key = "show_package_name_in_app_details"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.show_last_update_time_in_app_details)
                key = "show_last_update_time_in_app_details"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_long_press_to_copy_in_app_details)
                key = "enable_long_press_to_copy_in_app_details"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.click_icon_open_market_page)
                key = "click_icon_open_market_page"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) requireActivity().openApp(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class Battery : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.oplus.battery")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.open_battery_health)
                summary = getString(R.string.open_battery_health_summary)
                key = "open_battery_health"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "open_battery_health", false)) {
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.fix_battery_health_data_display)
                    summary = getString(R.string.fix_battery_health_data_display_summary)
                    key = "fix_battery_health_data_display"
                    setDefaultValue(false)
                    isVisible = SDK >= A13
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                })
                if (context.getBoolean(ModulePrefs, "fix_battery_health_data_display")) {
                    addPreference(EditTextPreference(context).apply {
                        title = "自定义电池设计容量"
                        dialogTitle = title
                        summary = context.getString(
                            ModulePrefs, "fix_battery_health_design_capacity", "0"
                        ) + " mAh"
                        key = "fix_battery_health_design_capacity"
                        setDefaultValue("0")
                        isVisible = false //SDK >= A13
                        isIconSpaceReserved = false
                    })
                }
            }
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.open_screen_power_save)
                summary = getString(R.string.open_screen_power_save_summary)
                key = "open_screen_power_save"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.performance_mode_and_standby_optimization)
                summary = getString(R.string.performance_mode_and_standby_optimization_summary)
                key = "performance_mode_and_standby_optimization"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_high_temperature_limit)
                summary = getString(R.string.remove_high_temperature_limit_summary)
                key = "remove_high_temperature_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.BatteryOptimization)
                key = "BatteryOptimization"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.restore_default_battery_optimization_whitelist)
                key = "restore_default_battery_optimization_whitelist"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            if (context.getBoolean(
                    ModulePrefs, "restore_default_battery_optimization_whitelist", false
                )
            ) {
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.disable_customize_battery_optimization_whiteList)
                    summary =
                        getString(R.string.disable_customize_battery_optimization_whiteList_summary)
                    key = "disable_customize_battery_optimization_whiteList"
                    setDefaultValue(false)
                    isVisible = false
                    isIconSpaceReserved = false
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) jumpBattery(requireActivity())
        return super.onOptionsItemSelected(item)
    }
}

class Camera : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.oneplus.camera", "com.oplus.camera")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_watermark_word_limit)
                key = "remove_watermark_word_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_hasselblad_watermark_style)
                key = "enable_hasselblad_watermark_style"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(EditTextPreference(context).apply {
                title = getString(R.string.custom_model_watermark)
                dialogTitle = title
                summary = context.getString(
                    ModulePrefs, "custom_model_watermark", "None"
                )
                if (summary.isNullOrBlank()) summary = "None"
                key = "custom_model_watermark"
                setDefaultValue("None")
                isIconSpaceReserved = false
                isVisible = Build.MODEL.contains("RM", true).not()
                setOnPreferenceChangeListener { _, newValue ->
                    summary = (newValue as String).ifBlank { "None" }
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.fix_hasselblad_custom_watermark_crash)
                summary = getString(R.string.fix_hasselblad_custom_watermark_crash_summary)
                key = "fix_hasselblad_custom_watermark_crash"
                setDefaultValue(false)
                isVisible = false
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_10_bit_image_support)
                summary = getString(R.string.enable_10_bit_image_support_summary)
                key = "enable_10_bit_image_support"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) requireActivity().openApp(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class OplusGames : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.oplus.games", "com.oplus.cosa")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_root_check)
                summary = getString(R.string.remove_root_check_summary)
                key = "remove_root_check"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_startup_animation)
                key = "remove_startup_animation"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_welfare_page)
                key = "remove_welfare_page"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_some_vip_limit)
                summary = getString(R.string.remove_some_vip_limit_summary)
                key = "remove_some_vip_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_developer_page)
                summary = getString(R.string.enable_developer_page_summary)
                key = "enable_developer_page"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(EditTextPreference(context).apply {
                title = getString(R.string.custom_media_player_support)
                dialogTitle = title
                summary = context.getString(
                    ModulePrefs, "custom_media_player_support", "None"
                )
                if (summary.isNullOrBlank()) summary = "None"
                dialogMessage = getString(R.string.custom_media_player_support_message)
                key = "custom_media_player_support"
                setDefaultValue("None")
                isIconSpaceReserved = false
                setOnBindEditTextListener {
                    it.setText((summary as String).replaceBlankLine)
                }
                setOnPreferenceChangeListener { _, newValue ->
                    val format = (newValue as String).replaceBlankLine
                    summary = format.ifBlank { "None" }
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_danmaku_notification_whitelist)
                key = "remove_danmaku_notification_whitelist"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_game_voice_changer_whitelist)
                key = "remove_game_voice_changer_whitelist"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_x_mode_feature)
                key = "enable_x_mode_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_gt_mode_feature)
                key = "enable_gt_mode_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_one_plus_characteristic)
                key = "enable_one_plus_characteristic"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_adreno_gpu_controller)
                key = "enable_adreno_gpu_controller"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_support_competition_mode)
                key = "enable_support_competition_mode"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_competition_mode_sound)
                key = "remove_competition_mode_sound"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_increase_fps_limit_feature)
                key = "enable_increase_fps_limit_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_increase_fps_feature)
                key = "enable_increase_fps_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_optimise_power_feature)
                key = "enable_optimise_power_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_super_resolution_feature)
                key = "enable_super_resolution_feature"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
            isVisible =
                requireActivity().checkPackName("com.oplus.games") && requireActivity().checkResolveActivity(
                    Intent().setClassName(
                        "com.oplus.games", "business.compact.activity.GameBoxCoverActivity"
                    )
                )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) ShellUtils.execCommand(
            "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true
        )
        return super.onOptionsItemSelected(item)
    }
}

class ThemeStore : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.heytap.themestore")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.unlock_themestore_vip)
                summary = getString(R.string.unlock_themestore_vip_summary)
                key = "unlock_themestore_vip"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) requireActivity().openApp(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class CloudService : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.heytap.cloud")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_network_limit)
                summary = getString(R.string.remove_network_limit_summary)
                key = "remove_network_limit"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) requireActivity().openApp(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class OplusOta : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.oplus.ota")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(Preference(context).apply {
                title = getString(R.string.unlock_local_upgrade)
                summary = getString(R.string.unlock_local_upgrade_summary)
                key = "unlock_local_upgrade"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    scopeLife {
                        val command = arrayOf(
                            "settings put global development_settings_enabled 1",
                            "pm clear com.oplus.ota",
                            "settings put global airplane_mode_on 1",
                            "am broadcast --user all -a android.intent.action.AIRPLANE_MODE --ez 'state' 'true'",
                            "am start com.oplus.ota/com.oplus.otaui.activity.EntryActivity"
                        )
                        withDefault { ShellUtils.execCommand(command, true) }
                    }
                    true
                }
            })
            addPreference(SwitchPreference(context).apply {
                val getStatus = ShellUtils.execCommand(
                    "getprop ro.boot.veritymode", false, true
                )
                val status = if (getStatus.result == 1) "null"
                else getStatus.successMsg.toString().ifBlank { "null" }
                title = getString(R.string.restore_ota_update_verity)
                summary = getString(R.string.restore_ota_update_verity_summary, status)
                key = "restore_ota_update_verity"
                isEnabled = status != "enforcing"
                isChecked = status == "enforcing"
                isPersistent = false
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, newValue ->
                    val value = if (newValue as Boolean) "enforcing" else "\"\""
                    val command = "resetprop ro.boot.veritymode $value"
                    context.toast(command)
                    val exec = ShellUtils.execCommand(command, true, true)
                    if (exec.result == 0) {
                        summary = getString(R.string.restore_ota_update_verity_summary, value)
                    } else (activity as MainActivity).restart()
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.extract_ota_information)
                summary = getString(R.string.extract_ota_information_summary)
                key = "extract_ota_information"
                setDefaultValue(false)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_oplusOta_to_extractOTAFragment, title)
                    true
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) jumpOTA(requireActivity())
        return super.onOptionsItemSelected(item)
    }
}

class OplusPictorial : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.heytap.pictorial")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_image_save_watermark)
                key = "remove_image_save_watermark"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_video_save_watermark)
                key = "remove_video_save_watermark"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) jumpPictorial(requireActivity())
        return super.onOptionsItemSelected(item)
    }
}

class OplusMMS : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.mms")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_verification_code_floating_window)
                key = "remove_verification_code_floating_window"
                setDefaultValue(false)
                isVisible = SDK >= A13
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class OplusBrowser : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.heytap.browser")

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_weather_page_ads)
                summary = getString(R.string.remove_weather_page_ads_summary)
                key = "remove_weather_page_ads"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class OplusGesture : ModulePreferenceFragment() {
    private val scopes = arrayOf("com.android.systemui", "com.oplus.gesture")
    private val loadLeftImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val path = FileUtils.getDocumentPath(requireActivity(), it)
            requireActivity().putString(
                ModulePrefs, "replace_side_slider_icon_on_left", path
            )
            findPreference<Preference>("replace_side_slider_icon_on_left")?.summary = path
        }
    }
    private val loadRightImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val path = FileUtils.getDocumentPath(requireActivity(), it)
            requireActivity().putString(
                ModulePrefs, "replace_side_slider_icon_on_right", path
            )
            findPreference<Preference>("replace_side_slider_icon_on_right")?.summary = path
        }
    }

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.enable_volume_key_control_flashlight)
                summary = getString(R.string.need_restart_system)
                key = "enable_volume_key_control_flashlight"
                setDefaultValue(false)
                isVisible = getOSVersionCode >= 27
                isIconSpaceReserved = false
            })

            if (SDK >= A13) {
                addPreference(PreferenceCategory(context).apply {
                    title = getString(R.string.AonGesture)
                    key = "AonGesture"
                    isIconSpaceReserved = false
                })
                addPreference(SwitchPreference(context).apply {
                    title = getString(R.string.force_enable_aon_gestures)
                    summary = getString(R.string.force_enable_aon_gestures_summary)
                    key = "force_enable_aon_gestures"
                    setDefaultValue(false)
                    isEnabled =
                        context.checkPackName("com.oplus.gesture") && context.checkPackName("com.aiunit.aon")
                    isIconSpaceReserved = false
                })
                addPreference(EditTextPreference(context).apply {
                    title = getString(R.string.custom_aon_gesture_scroll_page_whitelist)
                    dialogTitle = title
                    summary = context.getString(
                        ModulePrefs, "custom_aon_gesture_scroll_page_whitelist", "None"
                    )
                    if (summary.isNullOrBlank()) summary = "None"
                    dialogMessage = getString(R.string.custom_aon_gesture_whitelist_tips)
                    key = "custom_aon_gesture_scroll_page_whitelist"
                    setDefaultValue("None")
                    isEnabled = context.checkPackName("com.aiunit.aon")
                    isIconSpaceReserved = false
                    setOnBindEditTextListener {
                        it.setText((summary as String).replaceBlankLine)
                    }
                    setOnPreferenceChangeListener { _, newValue ->
                        val format = (newValue as String).replaceBlankLine
                        summary = format.ifBlank { "None" }
                        true
                    }
                })
                addPreference(EditTextPreference(context).apply {
                    title = getString(R.string.custom_aon_gesture_video_whitelist)
                    dialogTitle = title
                    summary = context.getString(
                        ModulePrefs, "custom_aon_gesture_video_whitelist", "None"
                    )
                    if (summary.isNullOrBlank()) summary = "None"
                    dialogMessage = getString(R.string.custom_aon_gesture_whitelist_tips)
                    key = "custom_aon_gesture_video_whitelist"
                    setDefaultValue("None")
                    isEnabled = context.checkPackName("com.aiunit.aon")
                    isVisible = false //SDK >= A13
                    isIconSpaceReserved = false
                    setOnBindEditTextListener {
                        it.setText((summary as String).replaceBlankLine)
                    }
                    setOnPreferenceChangeListener { _, newValue ->
                        val format = (newValue as String).replaceBlankLine
                        summary = format.ifBlank { "None" }
                        true
                    }
                })
            }

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.FullScreenGestureRelated)
                key = "FullScreenGestureRelated"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_side_slider)
                key = "remove_side_slider"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_side_slider_black_background)
                key = "remove_side_slider_black_background"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.remove_rotate_screen_button)
                key = "remove_rotate_screen_button"
                setDefaultValue(false)
                isIconSpaceReserved = false
            })

            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.CustomSideSliderIcon)
                key = "CustomSideSliderIcon"
                isIconSpaceReserved = false
            })
            addPreference(SwitchPreference(context).apply {
                title = getString(R.string.replace_side_slider_icon_switch)
                summary = getString(R.string.replace_side_slider_icon_switch_summary)
                key = "replace_side_slider_icon_switch"
                isIconSpaceReserved = false
                setOnPreferenceChangeListener { _, _ ->
                    (activity as MainActivity).restart()
                    true
                }
            })
            if (context.getBoolean(ModulePrefs, "replace_side_slider_icon_switch", false)) {
                addPreference(Preference(context).apply {
                    title = getString(R.string.replace_side_slider_icon_on_left)
                    key = "replace_side_slider_icon_on_left"
                    summary = context.getString(
                        ModulePrefs, "replace_side_slider_icon_on_left", "null"
                    )
                    isIconSpaceReserved = false
                    isCopyingEnabled = true
                    setOnPreferenceClickListener {
                        loadLeftImage.launch("image/*")
                        true
                    }
                })
                addPreference(Preference(context).apply {
                    title = getString(R.string.replace_side_slider_icon_on_right)
                    key = "replace_side_slider_icon_on_right"
                    summary = context.getString(
                        ModulePrefs, "replace_side_slider_icon_on_right", "null"
                    )
                    isIconSpaceReserved = false
                    isCopyingEnabled = true
                    setOnPreferenceClickListener {
                        loadRightImage.launch("image/*")
                        true
                    }
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.common_words_open)).apply {
            setIcon(R.drawable.baseline_open_in_new_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        if (item.itemId == 2) jumpGesture(requireActivity())
        return super.onOptionsItemSelected(item)
    }
}