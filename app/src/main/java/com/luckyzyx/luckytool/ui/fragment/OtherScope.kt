package com.luckyzyx.luckytool.ui.fragment

import android.os.Bundle
import androidx.preference.SwitchPreference
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.fragment.base.BaseScopePreferenceFeagment
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.openApp

class Everyimage : BaseScopePreferenceFeagment() {
    override val scopes = arrayOf("com.east2d.everyimage")
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

    override fun isEnableRestartMenu(): Boolean = true
    override fun isEnableOpenMenu(): Boolean = true
    override fun callOpenMenu() = requireActivity().openApp(scopes)

}

class AlphaBackupPro : BaseScopePreferenceFeagment() {
    override val scopes = arrayOf("com.ruet_cse_1503050.ragib.appbackup.pro")
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

    override fun isEnableRestartMenu(): Boolean = true
    override fun isEnableOpenMenu(): Boolean = true
    override fun callOpenMenu() = requireActivity().openApp(scopes)
}

class KsWeb : BaseScopePreferenceFeagment() {
    override val scopes = arrayOf("ru.kslabs.ksweb")
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

    override fun isEnableRestartMenu(): Boolean = true
    override fun isEnableOpenMenu(): Boolean = true
    override fun callOpenMenu() = requireActivity().openApp(scopes)
}

class ADM : BaseScopePreferenceFeagment() {
    override val scopes = arrayOf("com.dv.adm")
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ModulePrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(context).apply {
                    title = getString(R.string.adm_unlock_pro)
                    key = "adm_unlock_pro"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }

    override fun isEnableRestartMenu(): Boolean = true
    override fun isEnableOpenMenu(): Boolean = true
    override fun callOpenMenu() = requireActivity().openApp(scopes)
}