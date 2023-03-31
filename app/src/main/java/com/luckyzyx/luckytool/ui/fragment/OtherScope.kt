package com.luckyzyx.luckytool.ui.fragment

import android.os.Bundle
import androidx.preference.SwitchPreference
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

class Everyimage : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.skip_startup_page)
                    key = "skip_startup_page"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.vip_download)
                    key = "vip_download"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class AlphaBackupPro : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_pro_license)
                    key = "remove_check_license"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class KsWeb : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.remove_pro_license)
                    key = "ksweb_remove_check_license"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}