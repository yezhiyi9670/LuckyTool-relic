package com.luckyzyx.luckytool.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.data.formatDate
import com.luckyzyx.luckytool.utils.data.getDocumentPath
import com.luckyzyx.luckytool.utils.tools.*

class Android : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.ColorOSCorePatchTip)
                    key = "ColorOSCorePatchTip"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    setTitle(R.string.corepatch)
                    setSummary(R.string.corepatch_summary)
                    key = "CorePatch"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    setTitle(R.string.downgr)
                    setSummary(R.string.downgr_summary)
                    key = "downgrade"
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    setTitle(R.string.authcreak)
                    setSummary(R.string.authcreak_summary)
                    key = "authcreak"
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    setTitle(R.string.digestCreak)
                    setSummary(R.string.digestCreak_summary)
                    key = "digestCreak"
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    setTitle(R.string.UsePreSig)
                    setSummary(R.string.UsePreSig_summary)
                    key = "UsePreSig"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        if (newValue == true) {
                            MaterialAlertDialogBuilder(context)
                                .setMessage(R.string.usepresig_warn)
                                .setPositiveButton("OK", null)
                                .show()
                        }
                        true
                    }
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    setTitle(R.string.enhancedMode)
                    setSummary(R.string.enhancedMode_summary)
                    key = "enhancedMode"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class StatusBar : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarClock)
                    summary =
                        getString(R.string.statusbar_clock_show_second) + "," + getString(R.string.statusbar_clock_show_doublerow) + "," + getString(
                            R.string.statusbar_clock_doublerow_fontsize
                        )
                    key = "StatusBarClock"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarClock,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarNetWorkSpeed)
                    summary =
                        getString(R.string.enable_double_row_network_speed) + "," + getString(R.string.set_network_speed)
                    key = "StatusBarNetWorkSpeed"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarNetWorkSpeed, Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarNotice)
                    summary =
                        getString(R.string.remove_statusbar_top_notification) + "," + getString(R.string.remove_charging_completed)
                    key = "StatusBarNotice"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarNotice, Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarIcon)
                    summary =
                        getString(R.string.remove_statusbar_battery_percent) + "," + getString(R.string.remove_green_dot_privacy_prompt)
                    key = "StatusBarIcon"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarIcon, Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarControlCenter)
                    summary =
                        getString(R.string.control_center_clock_show_second) + "," + getString(R.string.remove_control_center_clock_red_one)
                    key = "StatusBarControlCenter"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarControlCenter, Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarBatteryInformation)
                    summary =
                        getString(R.string.battery_information_show) + "," + getString(R.string.battery_information_show_charge)
                    key = "StatusBarBatteryInfo"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarBatteryInfo, Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.StatusBarLayout)
                    summary =
                        getString(R.string.statusbar_layout_mode) + "," + getString(R.string.statusbar_layout_compatible_mode)
                    key = "StatusBarLayout"
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_statusBar_to_statusBarLayout, Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
        }
    }
}

class StatusBarClock : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                DropDownPreference(context).apply {
                    title = getString(R.string.statusbar_clock_enable)
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
                }
            )
            if (context.getString(ModulePrefs, "statusbar_clock_mode", "0") == "1") {
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_year)
                        key = "statusbar_clock_show_year"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_month)
                        key = "statusbar_clock_show_month"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_day)
                        key = "statusbar_clock_show_day"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_week)
                        key = "statusbar_clock_show_week"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_period)
                        key = "statusbar_clock_show_period"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_double_hour)
                        key = "statusbar_clock_show_double_hour"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_second)
                        key = "statusbar_clock_show_second"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_hide_spaces)
                        key = "statusbar_clock_hide_spaces"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_show_doublerow)
                        key = "statusbar_clock_show_doublerow"
                        setDefaultValue(false)
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_center_alignment)
                        key = "statusbar_clock_center_alignment"
                        setDefaultValue(false)
                        isVisible = false
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            requireActivity().dataChannel("com.android.systemui")
                                .put("statusbar_clock_center_alignment", newValue)
                            true
                        }
                    }
                )
                addPreference(
                    SeekBarPreference(context).apply {
                        title = getString(R.string.statusbar_clock_singlerow_fontsize)
                        summary = getString(R.string.statusbar_clock_fontsize_summary)
                        key = "statusbar_clock_singlerow_fontsize"
                        setDefaultValue(0)
                        max = 18
                        min = 0
                        seekBarIncrement = 1
                        showSeekBarValue = true
                        updatesContinuously = false
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            requireActivity().dataChannel("com.android.systemui")
                                .put("statusbar_clock_singlerow_fontsize", newValue)
                            true
                        }
                    }
                )
                addPreference(
                    SeekBarPreference(context).apply {
                        title = getString(R.string.statusbar_clock_doublerow_fontsize)
                        summary = getString(R.string.statusbar_clock_fontsize_summary)
                        key = "statusbar_clock_doublerow_fontsize"
                        setDefaultValue(0)
                        max = 10
                        min = 0
                        seekBarIncrement = 1
                        showSeekBarValue = true
                        updatesContinuously = false
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            requireActivity().dataChannel("com.android.systemui")
                                .put("statusbar_clock_doublerow_fontsize", newValue)
                            true
                        }
                    }
                )
            }
            if (context.getString(ModulePrefs, "statusbar_clock_mode", "0") == "2") {
                addPreference(
                    EditTextPreference(context).apply {
                        title = getString(R.string.statusbar_clock_custom_format)
                        dialogTitle = getString(R.string.statusbar_clock_custom_format)
                        summary = context.getString(
                            ModulePrefs,
                            "statusbar_clock_custom_format",
                            "HH:mm:ss"
                        )
                        dialogMessage = """
                            YYYY/MM/DD -> ${formatDate("YYYY/MM/DD")}
                            Y/M/D/E/a -> ${formatDate("Y/M/D/E/a")}
                            YY/YYYY -> ${formatDate("YY/YYYY")}
                            M/MM/MMM/MMMM/MMMMM -> ${formatDate("M/MM/MMM/MMMM/MMMMM")}
                            D/DD -> ${formatDate("D/DD")}
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
                        """.trimIndent()
                        key = "statusbar_clock_custom_format"
                        setDefaultValue("HH:mm:ss")
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            summary = newValue as String
                            requireActivity().dataChannel("com.android.systemui")
                                .put("statusbar_clock_custom_format", newValue)
                            true
                        }
                    }
                )
                addPreference(
                    SwitchPreference(context).apply {
                        title = getString(R.string.statusbar_clock_center_alignment)
                        key = "statusbar_clock_center_alignment"
                        setDefaultValue(false)
                        isVisible = false
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            requireActivity().dataChannel("com.android.systemui")
                                .put("statusbar_clock_center_alignment", newValue)
                            true
                        }
                    }
                )
                addPreference(
                    SeekBarPreference(context).apply {
                        title = getString(R.string.statusbar_clock_custom_fontsize)
                        summary = getString(R.string.statusbar_clock_fontsize_summary)
                        key = "statusbar_clock_custom_fontsize"
                        setDefaultValue(0)
                        max = 20
                        min = 0
                        seekBarIncrement = 1
                        showSeekBarValue = true
                        updatesContinuously = false
                        isIconSpaceReserved = false
                        setOnPreferenceChangeListener { _, newValue ->
                            requireActivity().dataChannel("com.android.systemui")
                                .put("statusbar_clock_custom_fontsize", newValue)
                            true
                        }
                    }
                )
            }
        }
    }
}

class StatusBarNetWorkSpeed : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.set_network_speed)
                    key = "set_network_speed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_double_row_network_speed)
                    key = "enable_double_row_network_speed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.set_network_speed_font_size)
                    key = "set_network_speed_font_size"
                    setDefaultValue(7)
                    max = 8
                    min = 0
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.set_network_speed_padding_bottom)
                    key = "set_network_speed_padding_bottom"
                    setDefaultValue(2)
                    max = 4
                    min = 0
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                }
            )
        }
        findPreference<SeekBarPreference>("set_network_speed_font_size")?.dependency =
            "enable_double_row_network_speed"
        findPreference<SeekBarPreference>("set_network_speed_padding_bottom")?.dependency =
            "enable_double_row_network_speed"
    }
}

class StatusBarNotify : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_statusbar_top_notification)
                    summary = getString(R.string.remove_statusbar_top_notification_summary)
                    key = "remove_statusbar_top_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_vpn_active_notification)
                    summary = getString(R.string.remove_vpn_active_notification_summary)
                    key = "remove_vpn_active_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_statusbar_devmode)
                    key = "remove_statusbar_devmode"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_charging_completed)
                    key = "remove_charging_completed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_statusbar_bottom_networkwarn)
                    summary = getString(R.string.remove_statusbar_bottom_networkwarn_summary)
                    key = "remove_statusbar_bottom_networkwarn"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_flashlight_open_notification)
                    key = "remove_flashlight_open_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_app_high_battery_consumption_warning)
                    summary =
                        getString(R.string.remove_app_high_battery_consumption_warning_summary)
                    key = "remove_app_high_battery_consumption_warning"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_high_performance_mode_notifications)
                    key = "remove_high_performance_mode_notifications"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_do_not_disturb_mode_notification)
                    key = "remove_do_not_disturb_mode_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_hotspot_power_consumption_notification)
                    summary =
                        getString(R.string.remove_hotspot_power_consumption_notification_summary)
                    key = "remove_hotspot_power_consumption_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_smart_rapid_charging_notification)
                    key = "remove_smart_rapid_charging_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class StatusBarIcon : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.StatusBarTopSmallIcon)
                    key = "StatusBarTopSmaliIcon"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_wifi_data_inout)
                    key = "remove_wifi_data_inout"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_mobile_data_inout)
                    key = "remove_mobile_data_inout"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_mobile_data_icon)
                    key = "remove_mobile_data_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_high_performance_mode_icon)
                    key = "remove_high_performance_mode_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_statusbar_securepayment_icon)
                    key = "remove_statusbar_securepayment_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_statusbar_battery_percent)
                    key = "remove_statusbar_battery_percent"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_green_dot_privacy_prompt)
                    key = "remove_green_dot_privacy_prompt"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_green_capsule_prompt)
                    key = "remove_green_capsule_prompt"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.StatusBarSmallIconStatus)
                    key = "StatusBarSmallIconStatus"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.status_bar_icon_vertical_center)
                    key = "status_bar_icon_vertical_center"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class StatusBarControlCenter : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.ControlCenter_Clock_Related)
                    key = "ControlCenter_Clock_Related"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.control_center_clock_show_second)
                    key = "control_center_clock_show_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.fix_clock_colon_style)
                    summary = getString(R.string.fix_clock_colon_style_summary)
                    key = "fix_clock_colon_style"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_control_center_clock_red_one)
                    key = "remove_control_center_clock_red_one"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_control_center_date_comma)
                    key = "remove_control_center_date_comma"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.ControlCenter_UI_Related)
                    key = "ControlCenter_UI_Related"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_control_center_user_switcher)
                    key = "remove_control_center_user_switcher"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK < A13
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_control_center_mydevice)
                    key = "remove_control_center_mydevice"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.force_display_media_player)
                    key = "force_display_media_player"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.ControlCenter_Tile_Related)
                    key = "ControlCenter_Tile_Related"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.control_center_tile_enable)
                    key = "control_center_tile_enable"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_unexpanded_columns_vertical)
                    key = "tile_unexpanded_columns_vertical"
                    setDefaultValue(6)
                    max = 6
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_unexpanded_columns_horizontal)
                    key = "tile_unexpanded_columns_horizontal"
                    setDefaultValue(6)
                    max = 8
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_columns_vertical)
                    key = "tile_expanded_columns_vertical"
                    setDefaultValue(4)
                    max = 7
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_columns_horizontal)
                    key = "tile_expanded_columns_horizontal"
                    setDefaultValue(6)
                    max = 9
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK < 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_unexpanded_columns_vertical)
                    key = "tile_unexpanded_columns_vertical_c13"
                    setDefaultValue(5)
                    max = 6
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_rows_vertical)
                    key = "tile_expanded_rows_vertical_c13"
                    setDefaultValue(3)
                    max = 6
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_expanded_columns_vertical)
                    key = "tile_expanded_columns_vertical_c13"
                    setDefaultValue(4)
                    max = 7
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.tile_columns_horizontal_c13)
                    key = "tile_columns_horizontal_c13"
                    setDefaultValue(5)
                    max = 6
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                }
            )
        }
        findPreference<SeekBarPreference>("tile_unexpanded_columns_vertical")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_unexpanded_columns_horizontal")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_expanded_columns_vertical")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_expanded_columns_horizontal")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_unexpanded_columns_vertical_c13")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_expanded_rows_vertical_c13")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_expanded_columns_vertical_c13")?.dependency =
            "control_center_tile_enable"
        findPreference<SeekBarPreference>("tile_columns_horizontal_c13")?.dependency =
            "control_center_tile_enable"
    }
}

class StatusBarLayout : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                DropDownPreference(context).apply {
                    title = getString(R.string.statusbar_layout_mode)
                    summary = "%s"
                    key = "statusbar_layout_mode"
                    entries = resources.getStringArray(R.array.statusbar_layout_mode_entries)
                    entryValues = arrayOf("0", "1")
                    setDefaultValue("0")
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.statusbar_layout_compatible_mode)
                    key = "statusbar_layout_compatible_mode"
                    setDefaultValue(false)
                    isEnabled = context.getString(ModulePrefs, "statusbar_layout_mode", "0") == "1"
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, _ ->
                        (activity as MainActivity).restart()
                        true
                    }
                }
            )
            if (context.getString(
                    ModulePrefs, "statusbar_layout_mode", "0"
                ) == "1"
                && context.getBoolean(
                    ModulePrefs, "statusbar_layout_compatible_mode", false
                )
            ) {
                addPreference(
                    SeekBarPreference(context).apply {
                        title = getString(R.string.statusbar_layout_left_margin)
                        summary = getString(R.string.statusbar_layout_margin_tip)
                        key = "statusbar_layout_left_margin"
                        setDefaultValue(0)
                        max = 150
                        min = 0
                        seekBarIncrement = 1
                        showSeekBarValue = true
                        updatesContinuously = false
                        isIconSpaceReserved = false
                    }
                )
                addPreference(
                    SeekBarPreference(context).apply {
                        title = getString(R.string.statusbar_layout_right_margin)
                        summary = getString(R.string.statusbar_layout_margin_tip)
                        key = "statusbar_layout_right_margin"
                        setDefaultValue(0)
                        max = 150
                        min = 0
                        seekBarIncrement = 1
                        showSeekBarValue = true
                        updatesContinuously = false
                        isIconSpaceReserved = false
                    }
                )
            }
        }
    }
}

class StatusBarBatteryInfo : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.battery_information_show)
                    summary = getString(R.string.battery_information_show_summary)
                    key = "battery_information_show"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        requireActivity().dataChannel(packageName = "com.android.systemui")
                            .put(key = "battery_information_show", value = newValue)
                        true
                    }
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.battery_information_show_charge)
                    summary = getString(R.string.battery_information_show_charge_summary)
                    key = "battery_information_show_charge"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        requireActivity().dataChannel(packageName = "com.android.systemui")
                            .put(key = "battery_information_show_charge", value = newValue)
                        true
                    }
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.battery_information_show_update_time)
                    summary = getString(R.string.battery_information_show_update_time_summary)
                    key = "battery_information_show_update_time"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        requireActivity().dataChannel(packageName = "com.android.systemui")
                            .put(key = "battery_information_show_update_time", value = newValue)
                        true
                    }
                }
            )
        }
        findPreference<SwitchPreference>("battery_information_show_charge")?.dependency =
            "battery_information_show"
        findPreference<SwitchPreference>("battery_information_show_update_time")?.dependency =
            "battery_information_show"
    }
}

class Desktop : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_alarmclock_widget_redone)
                    key = "remove_alarmclock_widget_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_recent_task_list_clear_button)
                    key = "remove_recent_task_list_clear_button"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_appicon_dot)
                    key = "remove_appicon_dot"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.set_folder_layout_4x4)
                    key = "set_folder_layout_4x4"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.launcher_layout_related)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.launcher_layout_enable)
                    summary = getString(R.string.launcher_layout_row_colume)
                    key = "launcher_layout_enable"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.launcher_layout_max_rows)
                    key = "launcher_layout_max_rows"
                    setDefaultValue(6)
                    max = 10
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.launcher_layout_max_columns)
                    key = "launcher_layout_max_columns"
                    setDefaultValue(4)
                    max = 8
                    min = 1
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                }
            )
        }
        findPreference<SeekBarPreference>("launcher_layout_max_rows")?.dependency =
            "launcher_layout_enable"
        findPreference<SeekBarPreference>("launcher_layout_max_columns")?.dependency =
            "launcher_layout_enable"
    }
}

class LockScreen : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_lock_screen_redone)
                    key = "remove_lock_screen_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.set_lock_screen_centered)
                    summary = getString(R.string.set_lock_screen_centered_summary)
                    key = "set_lock_screen_centered"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_top_lock_screen_icon)
                    key = "remove_top_lock_screen_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_lock_screen_bottom_left_button)
                    key = "remove_lock_screen_bottom_left_button"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_lock_screen_bottom_right_camera)
                    key = "remove_lock_screen_bottom_right_camera"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_lock_screen_bottom_sos_button)
                    summary = getString(R.string.remove_lock_screen_bottom_sos_button_summary)
                    key = "remove_lock_screen_bottom_sos_button"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK >= 33
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_72hour_password_verification)
                    summary = getString(R.string.remove_72hour_password_verification_summary)
                    key = "remove_72hour_password_verification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Screenshot : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_system_screenshot_delay)
                    summary = getString(R.string.remove_system_screenshot_delay_summary)
                    key = "remove_system_screenshot_delay"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_screenshot_privacy_limit)
                    summary = getString(R.string.remove_screenshot_privacy_limit_summary)
                    key = "remove_screenshot_privacy_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.disable_flag_secure)
                    summary = getString(R.string.disable_flag_secure_summary)
                    key = "disable_flag_secure"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Application : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.AppStartupRelated)
                    key = "AppStartupRelated"
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.disable_splash_screen)
                    key = "disable_splash_screen"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.APPRelatedList)
                    key = "APPRelatedList"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.zoom_window_support_list)
                    summary = getString(R.string.zoom_window_support_list_summary)
                    key = "zoom_window_support_list"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_application_to_zoomWindowFragment,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.dark_mode_support_list)
                    summary = getString(R.string.zoom_window_support_list_summary)
                    key = "dark_mode_support_list"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_application_to_darkModeFragment,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.multi_app_custom_list)
                    summary = getString(R.string.multi_app_custom_list_summary)
                    key = "multi_app_custom_list"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_application_to_multiFragment,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.AppInstallationRelated)
                    summary = getString(R.string.PackageInstaller_summary)
                    key = "PackageInstaller"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.skip_apk_scan)
                    summary = getString(R.string.skip_apk_scan_summary)
                    key = "skip_apk_scan"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.allow_downgrade_install)
                    summary = getString(R.string.allow_downgrade_install_summary)
                    key = "allow_downgrade_install"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_install_ads)
                    summary = getString(R.string.remove_install_ads_summary)
                    key = "remove_install_ads"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.replase_aosp_installer)
                    summary = getString(R.string.replase_aosp_installer_summary)
                    key = "replase_aosp_installer"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_adb_install_confirm)
                    summary = getString(R.string.remove_adb_install_confirm_summary)
                    key = "remove_adb_install_confirm"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.ApplyOtherRestrictions)
                    key = "ApplyOtherRestrictions"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.unlock_startup_limit)
                    summary = getString(R.string.unlock_startup_limit_summary)
                    key = "unlock_startup_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.unlock_task_locks)
                    key = "unlock_task_locks"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class DialogRelated : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.disable_duplicate_floating_window)
                    summary = getString(R.string.disable_duplicate_floating_window_summary)
                    key = "disable_duplicate_floating_window"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = Build.VERSION.SDK_INT >= 33
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.disable_headphone_high_volume_warning)
                    summary = getString(R.string.disable_headphone_high_volume_warning_summary)
                    key = "disable_headphone_high_volume_warning"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_low_battery_dialog_warning)
                    summary = getString(R.string.remove_low_battery_dialog_warning_summary)
                    key = "remove_low_battery_dialog_warning"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_warning_dialog_that_app_runs_on_desktop)
                    summary =
                        getString(R.string.remove_warning_dialog_that_app_runs_on_desktop_summary)
                    key = "remove_warning_dialog_that_app_runs_on_desktop"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_usb_connect_dialog)
                    summary = getString(R.string.remove_usb_connect_dialog_summary)
                    key = "remove_usb_connect_dialog"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class FullScreenGestureRelated : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        val loadLeftImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val path = getDocumentPath(requireActivity(), it)
                requireActivity().putString(
                    ModulePrefs,
                    "replace_side_slider_icon_on_left",
                    path
                )
                findPreference<Preference>("replace_side_slider_icon_on_left")?.summary = path
            }
        }
        val loadRightImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val path = getDocumentPath(requireActivity(), it)
                requireActivity().putString(
                    ModulePrefs,
                    "replace_side_slider_icon_on_right",
                    path
                )
                findPreference<Preference>("replace_side_slider_icon_on_right")?.summary = path
            }
        }
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_side_slider)
                    key = "remove_side_slider"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_side_slider_black_background)
                    key = "remove_side_slider_black_background"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_rotate_screen_button)
                    key = "remove_rotate_screen_button"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(context).apply {
                    title = getString(R.string.CustomSideSliderIcon)
                    key = "CustomSideSliderIcon"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.replace_side_slider_icon_switch)
                    summary = getString(R.string.replace_side_slider_icon_switch_summary)
                    key = "replace_side_slider_icon_switch"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.replace_side_slider_icon_on_left)
                    key = "replace_side_slider_icon_on_left"
                    summary =
                        context.getString(
                            ModulePrefs,
                            "replace_side_slider_icon_on_left",
                            "null"
                        )
                    isIconSpaceReserved = false
                    isCopyingEnabled = true
                    setOnPreferenceClickListener {
                        loadLeftImage.launch("image/*")
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.replace_side_slider_icon_on_right)
                    key = "replace_side_slider_icon_on_right"
                    summary =
                        context.getString(
                            ModulePrefs,
                            "replace_side_slider_icon_on_right",
                            "null"
                        )
                    isIconSpaceReserved = false
                    isCopyingEnabled = true
                    setOnPreferenceClickListener {
                        loadRightImage.launch("image/*")
                        true
                    }
                }
            )
        }
        findPreference<Preference>("replace_side_slider_icon_on_left")?.dependency =
            "replace_side_slider_icon_switch"
        findPreference<Preference>("replace_side_slider_icon_on_right")?.dependency =
            "replace_side_slider_icon_switch"
    }
}

class FingerPrintRelated : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_fingerprint_icon)
                    key = "remove_fingerprint_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Miscellaneous : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.FloatingWindowDialogRelated)
                    summary =
                        getString(R.string.remove_low_battery_dialog_warning_summary) + "," + getString(
                            R.string.disable_headphone_high_volume_warning
                        )
                    key = "FloatingWindowDialogRelated"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_miscellaneous_to_dialogRelated,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.FullScreenGestureRelated)
                    summary =
                        getString(R.string.remove_side_slider) + "," + getString(R.string.remove_side_slider_black_background)
                    key = "FullScreenGestureRelated"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_miscellaneous_to_fullScreenGestureRelated,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }

                }
            )
            addPreference(
                Preference(context).apply {
                    title = getString(R.string.FingerPrintRelated)
                    summary = getString(R.string.remove_fingerprint_icon)
                    key = "FingerPrintRelated"
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(
                            R.id.action_miscellaneous_to_fingerPrintRelated,
                            Bundle().apply {
                                putCharSequence("title_label", title)
                            })
                        true
                    }

                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.show_charging_ripple)
                    summary = getString(R.string.show_charging_ripple_summary)
                    key = "show_charging_ripple"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = Build.VERSION.SDK_INT >= 31
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.disable_otg_auto_off)
                    summary = getString(R.string.disable_otg_auto_off_summary)
                    key = "disable_otg_auto_off"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.disable_dpi_reboot_recovery)
                    summary = getString(R.string.disable_dpi_reboot_recovery_summary)
                    key = "disable_dpi_reboot_recovery"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_storage_limit)
                    summary = getString(R.string.remove_storage_limit_summary)
                    key = "remove_storage_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SeekBarPreference(context).apply {
                    title = getString(R.string.media_volume_level)
                    summary = getString(R.string.media_volume_level_summary)
                    key = "media_volume_level"
                    setDefaultValue(0)
                    max = 30
                    min = 0
                    seekBarIncrement = 1
                    showSeekBarValue = true
                    updatesContinuously = false
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.open_battery_health)
                    summary = getString(R.string.open_battery_health_summary)
                    key = "open_battery_health"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.open_screen_power_save)
                    summary = getString(R.string.open_screen_power_save_summary)
                    key = "open_screen_power_save"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.performance_mode_and_standby_optimization)
                    summary =
                        getString(R.string.performance_mode_and_standby_optimization_summary)
                    key = "performance_mode_and_standby_optimization"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = SDK >= A13
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.allow_untrusted_touch)
                    key = "allow_untrusted_touch"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Camera : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_watermark_word_limit)
                    key = "remove_watermark_word_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_10_bit_image_support)
                    key = "enable_10_bit_image_support"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class OplusGames : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_root_check)
                    summary = getString(R.string.remove_root_check_summary)
                    key = "remove_root_check"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_startup_animation)
                    key = "remove_startup_animation"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_developer_page)
                    summary = getString(R.string.enable_developer_page_summary)
                    key = "enable_developer_page"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_genshin_impact_theme)
                    summary = getString(R.string.enable_genshin_impact_theme_summary)
                    key = "enable_genshin_impact_theme"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    isVisible = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_adreno_gpu_controller)
                    key = "enable_adreno_gpu_controller"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_support_competition_mode)
                    key = "enable_support_competition_mode"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_increase_fps_limit_feature)
                    key = "enable_increase_fps_limit_feature"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_increase_fps_feature)
                    key = "enable_increase_fps_feature"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_optimise_power_feature)
                    key = "enable_optimise_power_feature"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_gt_mode_feature)
                    key = "enable_gt_mode_feature"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.enable_super_resolution_feature)
                    key = "enable_super_resolution_feature"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class ThemeStore : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.unlock_themestore_vip)
                    summary = getString(R.string.unlock_themestore_vip_summary)
                    key = "unlock_themestore_vip"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class CloudService : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_network_limit)
                    summary = getString(R.string.remove_network_limit_summary)
                    key = "remove_network_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class OplusOta : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.unlock_local_upgrade)
                    summary = getString(R.string.unlock_local_upgrade_summary)
                    key = "unlock_local_upgrade"
                    setDefaultValue(false)
                    isVisible = false
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    val status = ShellUtils.execCommand(
                        "getprop ro.boot.veritymode",
                        true,
                        true
                    ).let {
                        if (it.result == 1) isEnabled = false else it.successMsg.ifBlank { "null" }
                    }
                    title = getString(R.string.remove_dm_verity)
                    summary = getString(R.string.remove_dm_verity_summary, status)
                    key = "remove_dm_verity"
                    isPersistent = false
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                    setOnPreferenceChangeListener { _, newValue ->
                        val value = newValue as Boolean
                        if (value && status == "enforcing") return@setOnPreferenceChangeListener true
                        ShellUtils.execCommand(
                            "resetprop ro.boot.veritymode ${if (value) "enforcing" else "\"\""}",
                            true
                        )
                        summary = getString(
                            R.string.remove_dm_verity_summary, ShellUtils.execCommand(
                                "getprop ro.boot.veritymode",
                                true,
                                true
                            ).successMsg.let { it.ifBlank { "null" } }
                        )
                        true
                    }
                }
            )
        }
    }
}