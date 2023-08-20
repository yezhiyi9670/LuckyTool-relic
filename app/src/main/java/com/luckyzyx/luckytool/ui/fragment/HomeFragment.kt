package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.YukiHookAPI
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentHomeBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.*
import com.luckyzyx.luckytool.utils.AppAnalyticsUtils.ckqcbss

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var enableModule: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    @Suppress("LocalVariableName")
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableModule = requireActivity().getBoolean(ModulePrefs, "enable_module", false)

        if (enableModule && YukiHookAPI.Status.isModuleActive) {
            binding.statusIcon.setImageResource(R.drawable.ic_round_check_24)
            binding.statusTitle.text = getString(R.string.module_isactivated)
        } else {
            binding.statusCard.setCardBackgroundColor(Color.GRAY)
            binding.statusIcon.setImageResource(R.drawable.ic_round_warning_24)
            binding.statusTitle.text = getString(R.string.module_notactive)
        }

        binding.statusSummary.apply {
            text = getString(R.string.module_version)
            text = "$text$getVersionName($getVersionCode)"
        }
        binding.enableModule.apply {
            text = context.getString(R.string.enable_module)
            isChecked = enableModule
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    context.putBoolean(ModulePrefs, "enable_module", isChecked)
                    (activity as MainActivity).restart()
                }
            }
        }

        if (requireActivity().getBoolean(
                SettingsPrefs,
                "auto_check_update",
                true
            )
        ) UpdateUtils(requireActivity()).checkUpdate(
            getVersionName,
            getVersionCode
        ) { versionName, versionCode, function ->
            if (getVersionCode < versionCode) {
                function()
                binding.updateView.apply {
                    isVisible = true
                    text = getString(R.string.check_update_hint) +
                            "  -->  $versionName($versionCode)"
                }
                binding.statusCard.setOnClickListener { function() }
            }
            binding.statusCard.apply {
                if (context.getBoolean(SettingsPrefs, "hidden_function", false)) {
                    setOnLongClickListener {
                        function()
                        true
                    }
                }
            }
        }

        binding.fpsTitle.text = getString(R.string.fps_title)
        binding.fpsSummary.text = getString(R.string.fps_summary)
        binding.fps.setOnClickListener {
            navigatePage(R.id.action_nav_home_to_forceFpsFragment, getString(R.string.fps_title))
        }

        binding.systemInfo.apply {
            text = context.getDeviceInfo()
            setOnLongClickListener {
                val isRealmeUI: Boolean
                val oplusOtaDialog = MaterialAlertDialogBuilder(context, dialogCentered).apply {
                    setTitle("OPLUS OTA")
                    setView(R.layout.layout_oplusota_dialog)
                }.show()
                val product_model =
                    oplusOtaDialog.findViewById<TextInputEditText>(R.id.oplusota_product_model)
                        ?.apply {
                            setText(getProp("ro.product.name"))
                            setOnLongClickListener {
                                context.copyStr(text as CharSequence)
                                true
                            }
                        }
                val ota_version =
                    oplusOtaDialog.findViewById<TextInputEditText>(R.id.oplusota_ota_version)
                        ?.apply {
                            setText(getProp("ro.build.version.ota"))
                            setOnLongClickListener {
                                context.copyStr(text as CharSequence)
                                true
                            }
                        }
                val realmeui_version_layout =
                    oplusOtaDialog.findViewById<TextInputLayout>(R.id.oplusota_realmeui_version_layout)
                val realmeui_version =
                    oplusOtaDialog.findViewById<TextInputEditText>(R.id.oplusota_realmeui_version)
                        ?.apply {
                            setText(getProp("ro.build.version.realmeui"))
                            setOnLongClickListener {
                                context.copyStr(text as CharSequence)
                                true
                            }
                        }
                if (realmeui_version?.text.toString()
                        .isBlank() || realmeui_version?.text.toString() == "null"
                ) {
                    isRealmeUI = false
                    realmeui_version_layout?.isVisible = false
                } else isRealmeUI = true
                val nv_identifier =
                    oplusOtaDialog.findViewById<TextInputEditText>(R.id.oplusota_nv_identifier)
                        ?.apply {
                            setText(getProp("ro.build.oplus_nv_id"))
                            setOnLongClickListener {
                                context.copyStr(text as CharSequence)
                                true
                            }
                        }
                val guid =
                    oplusOtaDialog.findViewById<TextInputEditText>(R.id.oplusota_guid)?.apply {
                        setText(getGuid)
                        setOnLongClickListener {
                            context.copyStr(text as CharSequence)
                            true
                        }
                    }
                oplusOtaDialog.findViewById<MaterialButton>(R.id.oplusota_copyall)?.apply {
                    setOnClickListener {
                        context.copyStr("ro.product.name -> ${product_model?.text}\nro.build.version.ota -> ${ota_version?.text}\n${if (isRealmeUI) "ro.build.version.realmeui -> ${realmeui_version?.text}\n" else ""}ro.build.oplus_nv_id -> ${nv_identifier?.text}\nguid -> ${guid?.text}\n")
                    }
                }
                true
            }
        }

        binding.donateTvView.setOnClickListener {
            val url = if (isZh(requireActivity())) "https://luckyzyx.github.io/LuckyTool_Doc/donate"
            else "https://luckyzyx.github.io/LuckyTool_Doc/en/donate"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.authorized.apply {
            if (isZh(context)) {
                isVisible = true
                text = context.getString(R.string.authorized)
                setTextColor(Color.RED)
            }
            setOnClickListener {
                val url = "https://luckyzyx.github.io/LuckyTool_Doc/use/download_link"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }

        binding.tv.apply {
            isVisible = false
            text = context.ckqcbss().toString()
//            text = getPackageDir("com.dragon.read").toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.menu_settings)).apply {
            setIcon(R.drawable.ic_baseline_info_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ThemeUtils.isNightMode(resources.configuration)) {
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartMain()
        if (item.itemId == 2) {
            MaterialAlertDialogBuilder(requireActivity()).apply {
                setTitle(getString(R.string.about_author))
                setView(MaterialTextView(context).apply {
                    var hideFunc = context.getBoolean(SettingsPrefs, "hidden_function", false)
                    setPadding(20.dp)
                    text = if (hideFunc) "忆清鸣、luckyzyx T" else "忆清鸣、luckyzyx"
                    setOnLongClickListener {
                        context.putBoolean(SettingsPrefs, "hidden_function", !hideFunc)
                        hideFunc = context.getBoolean(SettingsPrefs, "hidden_function", false)
                        text = if (hideFunc) "忆清鸣、luckyzyx T" else "忆清鸣、luckyzyx"
                        true
                    }
                })
                show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}