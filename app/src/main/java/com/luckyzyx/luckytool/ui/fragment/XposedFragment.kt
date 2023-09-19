package com.luckyzyx.luckytool.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.core.view.MenuProvider
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.drake.net.utils.scopeDialog
import com.drake.net.utils.scopeLife
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.AppAnalyticsUtils.ckqcbss
import com.luckyzyx.luckytool.utils.SDK
import com.luckyzyx.luckytool.utils.ThemeUtils
import com.luckyzyx.luckytool.utils.arraySummaryDot
import com.luckyzyx.luckytool.utils.checkPackName
import com.luckyzyx.luckytool.utils.dialogCentered
import com.luckyzyx.luckytool.utils.dp
import com.luckyzyx.luckytool.utils.fixIconSize
import com.luckyzyx.luckytool.utils.getAppLabel
import com.luckyzyx.luckytool.utils.getAppVersion
import com.luckyzyx.luckytool.utils.navigatePage
import com.luckyzyx.luckytool.utils.restartMain
import com.luckyzyx.luckytool.utils.setPrefsIconRes
import com.luckyzyx.luckytool.utils.setupMenuProvider
import kotlinx.coroutines.Dispatchers
import java.util.Arrays

class XposedFragment : ModulePreferenceFragment(), MenuProvider {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setupMenuProvider(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        init()
    }

    private fun init() {
        val dialog = MaterialAlertDialogBuilder(requireActivity(), dialogCentered).apply {
            setTitle(getString(R.string.common_words_loading))
            setView(LinearLayout(context).apply {
                addView(LinearProgressIndicator(context).apply {
                    layoutParams =
                        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    setPadding(20.dp, 20.dp, 20.dp, 0)
                    isIndeterminate = true
                })
            })
        }.create()
        scopeDialog(dialog, false, Dispatchers.Default) {
            if (preferenceScreen != null) return@scopeDialog
            val destination = findNavController().currentBackStack.value.lastOrNull()?.destination
            if (!destination.toString().contains(this@XposedFragment::class.java.simpleName)) {
                return@scopeDialog
            }
            createPreference()
        }
    }

    private fun createPreference() {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(Preference(context).apply {
                key = "android"
                setPrefsIconRes(android.R.mipmap.sym_def_app_icon) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.corepatch))
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_android, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "StatusBar"
                setPrefsIconRes("com.android.systemui") { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = getString(R.string.StatusBar)
                summary = arraySummaryDot(
                    getString(R.string.StatusBarNotice),
                    getString(R.string.StatusBarIcon),
                    getString(R.string.StatusBarClock)
                )
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_statusBar, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.android.launcher"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = getString(R.string.Desktop)
                summary = arraySummaryDot(
                    getString(R.string.AppBadgeRelated),
                    getString(R.string.FolderLayoutRelated),
                    getString(R.string.launcher_layout_related)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_launcher, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.oplus.aod"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = getString(R.string.AodRelated)
                summary = arraySummaryDot(
                    getString(R.string.remove_aod_music_whitelist),
                    getString(R.string.remove_aod_notification_icon_whitelist)
                )
                isVisible = SDK >= A13 && context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_aod, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "LockScreen"
                setPrefsIconRes("com.android.systemui") { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = getString(R.string.LockScreen)
                summary = arraySummaryDot(
                    getString(R.string.remove_lock_screen_redone),
                    getString(R.string.remove_lock_screen_bottom_right_camera)
                )
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_lockScreen, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.oplus.screenshot"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = getString(R.string.Screenshot)
                summary = arraySummaryDot(
                    getString(R.string.remove_system_screenshot_delay),
                    getString(R.string.remove_screenshot_privacy_limit)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_screenshot, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.android.packageinstaller"
                setPrefsIconRes(key) { resource, show ->
                    icon = fixIconSize(resource)
                    isIconSpaceReserved = show
                }
                title = getString(R.string.Application)
                summary = arraySummaryDot(
                    getString(R.string.skip_apk_scan), getString(R.string.unlock_startup_limit)
                )
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_application, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "Miscellaneous"
                setPrefsIconRes("com.android.systemui") { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = getString(R.string.Miscellaneous)
                summary = arraySummaryDot(getString(R.string.Miscellaneous_summary))
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_miscellaneous, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.oplus.battery"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.open_battery_health),
                    getString(R.string.open_screen_power_save)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_battery, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.android.settings"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.remove_top_account_display),
                    getString(R.string.remove_dpi_restart_recovery)
                )
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_settings, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.android.mms"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary =
                    arraySummaryDot(getString(R.string.remove_verification_code_floating_window))
                isVisible = SDK >= A13 && context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_oplusMMS, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.heytap.browser"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.remove_weather_page_ads))
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_oplusBrowser, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                val isOneplusCamera = context.checkPackName("com.oneplus.camera")
                key = if (isOneplusCamera) "com.oneplus.camera" else "com.oplus.camera"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.remove_watermark_word_limit),
                    getString(R.string.enable_10_bit_image_support)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_camera, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.coloros.gallery3d"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.enable_watermark_editing),
                    getString(R.string.replace_oneplus_model_watermark)
                )
                isVisible = SDK >= A13 && context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_oplusGallery, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.oplus.games"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.remove_root_check),
                    getString(R.string.enable_developer_page)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_oplusGames, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                val isHeytap = context.checkPackName("com.heytap.themestore")
                key = if (isHeytap) "com.heytap.themestore" else "com.oplus.themestore"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.unlock_themestore_vip))
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_themeStore, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.heytap.cloud"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.remove_network_limit))
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_cloudService, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.oplus.ota"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.unlock_local_upgrade),
                    getString(R.string.restore_ota_update_verity)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_oplusOta, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.heytap.pictorial"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.remove_image_save_watermark),
                    getString(R.string.remove_video_save_watermark)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_oplusPictorial, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.oplus.gesture"
                setPrefsIconRes(key) { resource, show ->
                    icon = fixIconSize(resource)
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(
                    getString(R.string.enable_volume_key_control_flashlight),
                    getString(R.string.force_enable_aon_gestures)
                )
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_oplusGesture, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.ruet_cse_1503050.ragib.appbackup.pro"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.remove_pro_license))
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_xposed_to_alphaBackupPro, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "ru.kslabs.ksweb"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.remove_pro_license))
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_ksWeb, title)
                    true
                }
            })
            addPreference(Preference(context).apply {
                key = "com.dv.adm"
                setPrefsIconRes(key) { resource, show ->
                    icon = resource
                    isIconSpaceReserved = show
                }
                title = context.getAppLabel(key)
                summary = arraySummaryDot(getString(R.string.adm_unlock_pro))
                isVisible = context.checkPackName(key)
                setOnPreferenceClickListener {
                    navigatePage(R.id.action_nav_function_to_ADM, title)
                    true
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().ckqcbss()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.menu_versioninfo)).apply {
            setIcon(R.drawable.ic_baseline_extension_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == 1) (activity as MainActivity).restartMain()
        if (menuItem.itemId == 2) requireActivity().bottomSheet()
        return true
    }

    private fun Context.bottomSheet() {
        scopeLife {
            val xposedScope = resources.getStringArray(R.array.xposed_scope)
            Arrays.sort(xposedScope)
            var str = getString(R.string.scope_version_info)
            xposedScope.forEach {
                val arrayList = getAppVersion(it)
                if (arrayList.isEmpty()) return@forEach
                str += "\n\n${getAppLabel(it)} - $it - ${arrayList[0]}(${arrayList[1]})[${arrayList[2]}]"
            }
            val nestedScrollView = NestedScrollView(this@bottomSheet).apply {
                setPadding(10.dp, 20.dp, 10.dp, 20.dp)
                addView(TextView(context).apply {
                    textSize = 16F
                    text = str
                })
            }
            val bottomSheetDialog = BottomSheetDialog(this@bottomSheet)
            bottomSheetDialog.setContentView(nestedScrollView)
            bottomSheetDialog.show()
        }
    }
}