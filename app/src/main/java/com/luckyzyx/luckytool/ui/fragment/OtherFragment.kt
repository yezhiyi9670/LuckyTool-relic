package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withDefault
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.IAdbDebugController
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentOtherBinding
import com.luckyzyx.luckytool.ui.service.AdbDebugControllerService
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.OtherPrefs
import com.luckyzyx.luckytool.utils.SettingsPrefs
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.ShortcutUtils
import com.luckyzyx.luckytool.utils.bindRootService
import com.luckyzyx.luckytool.utils.checkPackName
import com.luckyzyx.luckytool.utils.checkResolveActivity
import com.luckyzyx.luckytool.utils.copyStr
import com.luckyzyx.luckytool.utils.dialogCentered
import com.luckyzyx.luckytool.utils.getAppLabel
import com.luckyzyx.luckytool.utils.getBoolean
import com.luckyzyx.luckytool.utils.getString
import com.luckyzyx.luckytool.utils.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.jumpEngineermode
import com.luckyzyx.luckytool.utils.jumpRunningApp
import com.luckyzyx.luckytool.utils.navigatePage
import com.luckyzyx.luckytool.utils.putString
import com.luckyzyx.luckytool.utils.toast


class OtherFragment : Fragment() {

    private lateinit var binding: FragmentOtherBinding
    private var adbController: IAdbDebugController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initController()

        binding.quickEntry.setOnClickListener {
            navigatePage(R.id.action_nav_other_to_systemQuickEntry, getString(R.string.quick_entry))
        }

        binding.shortcut.apply {
            setOnClickListener {
                val dialog = MaterialAlertDialogBuilder(context, dialogCentered).apply {
                    setView(R.layout.layout_shortcut_dialog)
                }.show()
                val shortcutList = ShortcutUtils(context).getShortcutList()
                val keys = ArrayList<String>(shortcutList.keys)
                val titles = ArrayList<String>(shortcutList.values)
                dialog.findViewById<ListView>(R.id.shortcut_list)?.apply {
                    choiceMode = ListView.CHOICE_MODE_MULTIPLE
                    adapter = ArrayAdapter(
                        context, android.R.layout.simple_list_item_multiple_choice, titles
                    )
                    keys.forEach {
                        if (context.getBoolean(SettingsPrefs, it, false)) {
                            val index = keys.indexOf(it)
                            if (index != -1) setItemChecked(index, true)
                        }
                    }
                    onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
                        val key = keys[position]
                        val isChecked = (view as AppCompatCheckedTextView).isChecked
                        ShortcutUtils(context).setShortcutStatus(key, isChecked)
                        ShortcutUtils(context).setDynamicShortcuts()
                    }
                }
            }
        }

        binding.remoteAdbDebug.apply {
            setOnClickListener {
                val getPort = adbController?.adbPort ?: return@setOnClickListener
                var getIP = adbController?.wifiIP ?: "IP"

                val adbDialog = MaterialAlertDialogBuilder(context).apply {
                    setCancelable(true)
                    setView(R.layout.layout_adb_dialog)
                }.show()
                val adbPortLayout = adbDialog.findViewById<TextInputLayout>(R.id.adb_port_layout)
                val adbPort = adbDialog.findViewById<TextInputEditText>(R.id.adb_port)?.apply {
                    setText(
                        if (getPort == 0 || getPort == -1) {
                            context.getString(OtherPrefs, "adb_port", "6666")
                        } else getPort.toString()
                    )
                }
                val adbTv = adbDialog.findViewById<MaterialTextView>(R.id.adb_tv)?.apply {
                    if (getPort != 0 && getPort != -1) text = "adb connect $getIP:$getPort"
                    setOnLongClickListener {
                        context.copyStr(text.toString())
                        true
                    }
                }
                val adbTvTip = adbDialog?.findViewById<MaterialTextView>(R.id.adb_tv_tip)?.apply {
                    isVisible = adbTv?.text.isNullOrBlank().not()
                    setOnLongClickListener {
                        context.copyStr(adbTv?.text.toString())
                        true
                    }
                }
                adbDialog.findViewById<MaterialSwitch>(R.id.adb_switch)?.apply {
                    isEnabled = adbController != null
                    isChecked = isEnabled && getPort != 0 && getPort != -1
                    adbPortLayout?.isEnabled = isChecked.not()
                    setOnCheckedChangeListener { buttonView, checked ->
                        if (!buttonView.isPressed) return@setOnCheckedChangeListener
                        if (checked) {
                            val portStr = adbPort?.text
                            if (portStr.isNullOrBlank()) {
                                isChecked = false
                                adbTv?.text = context.getString(R.string.adb_debug_port_cannot_null)
                                return@setOnCheckedChangeListener
                            }
                            scopeLife {
                                val port = portStr.toString().toInt()
                                isEnabled = false
                                withDefault {
                                    adbController?.adbPort = port
                                    adbController?.restartAdb()
                                    getIP = adbController?.wifiIP ?: "IP"
                                    context.putString(OtherPrefs, "adb_port", port.toString())
                                }
                                adbPortLayout?.isEnabled = false
                                adbTv?.text = "adb connect $getIP:$portStr"
                                adbTvTip?.isVisible = true
                                isEnabled = true
                            }
                        } else scopeLife {
                            isEnabled = false
                            withDefault {
                                adbController?.adbPort = -1
                                adbController?.restartAdb()
                                adbController?.adbPort = 0
                            }
                            adbPortLayout?.isEnabled = true
                            adbTv?.text = ""
                            adbTvTip?.isVisible = false
                            isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun initController() {
        if (adbController == null) requireActivity().bindRootService(
            AdbDebugControllerService::class.java, { _: ComponentName?, iBinder: IBinder? ->
                adbController = IAdbDebugController.Stub.asInterface(iBinder)
                binding.remoteAdbDebug.isVisible = true
            })
    }
}

class SystemQuickEntry : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.SystemDebuggingRelated)
                key = "SystemDebuggingRelated"
                isIconSpaceReserved = false
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.engineering_mode)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    jumpEngineermode(context)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.charging_test)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    jumpBatteryInfo(context)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.developer_option)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -a com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS", true
                    )
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.system_interface_adjustment)
                isIconSpaceReserved = false
                isVisible = context.checkResolveActivity(
                    Intent().setClassName(
                        "com.android.systemui", "com.android.systemui.DemoMode"
                    )
                )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand("am start -n com.android.systemui/.DemoMode", true)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.AOSPSettingsPage)
                isIconSpaceReserved = false
                isVisible = context.checkResolveActivity(
                    Intent().setClassName(
                        "com.android.settings",
                        "com.android.settings.homepage.DeepLinkHomepageActivityInternal"
                    )
                )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -n com.android.settings/.homepage.DeepLinkHomepageActivityInternal",
                        true
                    )
                    true
                }
            })
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.HidePageRelated)
                key = "HidePageRelated"
                isIconSpaceReserved = false
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.process_manager)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    jumpRunningApp(context)
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.very_dark_mode)
                isIconSpaceReserved = false
                isVisible = context.checkResolveActivity(
                    Intent().setClassName(
                        "com.android.settings",
                        "com.android.settings.Settings\$ReduceBrightColorsSettingsActivity"
                    )
                )
                setOnPreferenceClickListener {
                    Intent().apply {
                        setClassName(
                            "com.android.settings",
                            "com.android.settings.Settings\$ReduceBrightColorsSettingsActivity"
                        )
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        startActivity(this)
                    }
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.battery_health)
                isIconSpaceReserved = false
                isVisible = context.checkResolveActivity(
                    Intent().setClassName(
                        "com.oplus.battery", "com.oplus.powermanager.fuelgaue.BatteryHealthActivity"
                    )
                )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -n com.oplus.battery/com.oplus.powermanager.fuelgaue.BatteryHealthActivity",
                        true
                    )
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.battery_optimization)
                isIconSpaceReserved = false
                setOnPreferenceClickListener {
                    Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        startActivity(this)
                    }
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.camera_algo_page)
                isIconSpaceReserved = false
                isVisible = context.checkResolveActivity(
                    Intent().setClassName(
                        "com.oplus.camera", "com.oplus.camera.ui.menu.algoswitch.AlgoSwitchActivity"
                    )
                )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -n com.oplus.camera/.ui.menu.algoswitch.AlgoSwitchActivity", true
                    )
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.browser_concise_mode)
                isIconSpaceReserved = false
                isVisible = context.checkPackName("com.heytap.browser")
                setOnPreferenceClickListener {
                    try {
                        Intent().apply {
                            setClassName(
                                "com.heytap.browser",
                                "com.heytap.browser.settings.component.BrowserPreferenceActivity"
                            )
                            putExtra(
                                "key.fragment.name",
                                "com.heytap.browser.settings.homepage.HomepagePreferenceFragment"
                            )
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                            startActivity(this)
                        }
                    } catch (ignore: Exception) {
                        context.toast("Error: Please check your browser version!")
                    }
                    true
                }
            })
            addPreference(PreferenceCategory(context).apply {
                title = getString(R.string.GameAssistantRelated)
                isIconSpaceReserved = false
                isVisible = context.checkPackName("com.oplus.games")
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.game_assistant_page)
                summary = "(${context.getAppLabel("com.oplus.games")})"
                isIconSpaceReserved = false
                isVisible = context.checkPackName("com.oplus.games") &&
                        context.checkResolveActivity(
                            Intent().setClassName(
                                "com.oplus.games",
                                "business.compact.activity.GameBoxCoverActivity"
                            )
                        )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity",
                        true
                    )
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.game_space_page)
                summary = "(${context.getAppLabel("com.nearme.gamecenter")})"
                isIconSpaceReserved = false
                isVisible =
                    context.checkPackName("com.nearme.gamecenter") && context.checkResolveActivity(
                        Intent().setClassName(
                            "com.nearme.gamecenter",
                            "com.nearme.gamespace.desktopspace.ui.DesktopSpaceMainActivity"
                        )
                    )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -n com.nearme.gamecenter/com.nearme.gamespace.desktopspace.ui.DesktopSpaceMainActivity",
                        true
                    )
                    true
                }
            })
            addPreference(Preference(context).apply {
                title = getString(R.string.game_assistant_develop_page)
                summary = "(${context.getAppLabel("com.oplus.games")})"
                isIconSpaceReserved = false
                isVisible = context.checkPackName("com.oplus.games") && context.getBoolean(
                    ModulePrefs, "enable_developer_page", false
                )
                setOnPreferenceClickListener {
                    ShellUtils.execCommand(
                        "am start -n com.oplus.games/business.compact.activity.GameDevelopOptionsActivity",
                        true
                    )
                    true
                }
            })
        }
    }
}