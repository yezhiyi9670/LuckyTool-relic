package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.ArraySet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drake.net.Get
import com.drake.net.utils.scopeLife
import com.drake.net.utils.scopeNetLife
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentDonateListBinding
import com.luckyzyx.luckytool.databinding.LayoutDonateItemBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.Base64CodeUtils
import com.luckyzyx.luckytool.utils.DCInfo
import com.luckyzyx.luckytool.utils.DInfo
import com.luckyzyx.luckytool.utils.DonateData
import com.luckyzyx.luckytool.utils.FileUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.OtherPrefs
import com.luckyzyx.luckytool.utils.SettingsPrefs
import com.luckyzyx.luckytool.utils.backupAllPrefs
import com.luckyzyx.luckytool.utils.base64Decode
import com.luckyzyx.luckytool.utils.base64Encode
import com.luckyzyx.luckytool.utils.clearAllPrefs
import com.luckyzyx.luckytool.utils.formatDate
import com.luckyzyx.luckytool.utils.getBoolean
import com.luckyzyx.luckytool.utils.getString
import com.luckyzyx.luckytool.utils.isZh
import com.luckyzyx.luckytool.utils.navigatePage
import com.luckyzyx.luckytool.utils.putBoolean
import com.luckyzyx.luckytool.utils.putInt
import com.luckyzyx.luckytool.utils.putString
import com.luckyzyx.luckytool.utils.putStringSet
import com.luckyzyx.luckytool.utils.setComponentDisabled
import com.luckyzyx.luckytool.utils.toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
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
                writeRestoreData(requireActivity(), FileUtils.readFromUri(requireActivity(), it))
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
                                    0 -> DonateData.showQRCode(context, Base64CodeUtils.qqCode)
                                    1 -> DonateData.showQRCode(context, Base64CodeUtils.wechatCode)
                                    2 -> DonateData.showQRCode(context, Base64CodeUtils.alipayCode)
                                    3 -> if (isZh(context)) {
                                        navigatePage(
                                            R.id.action_nav_setting_to_donateFragment,
                                            getString(R.string.donation_list)
                                        )
                                    } else startActivity(
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

                                    5 -> navigatePage(
                                        R.id.action_nav_setting_to_donateFragment,
                                        getString(R.string.donation_list)
                                    )
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
                        navigatePage(R.id.action_nav_setting_to_sourceFragment, title)
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

class DonateFragment : Fragment() {

    private lateinit var binding: FragmentDonateListBinding
    private lateinit var ddFile: File
    private var donateAdapter: DonateListAdapter? = null
    private val allData = ArrayList<DInfo>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonateListBinding.inflate(inflater)
        return binding.root
    }

    fun init(context: Context) {
        scopeLife {
            binding.searchViewLayout.apply {
                hint = "Name / PackageName"
                isHintEnabled = true
                isHintAnimationEnabled = true
            }
            binding.searchView.apply {
                isEnabled = false
                text = null
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        donateAdapter?.getFilter?.filter(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }

            binding.swipeRefreshLayout.apply {
                setOnRefreshListener { init(context) }
                isRefreshing = true
            }
            ddFile = File(context.filesDir.path + "/dd")
            if (ddFile.exists()) checkDonateData(context)
            else downloadJson(context, formatDate("YYYYMMddHHmm"))
        }
    }

    private fun checkDonateData(context: Context) {
        scopeNetLife {
            val latestUrl =
                "https://api.github.com/repos/luckyzyx/LuckyTool_Doc/releases/tags/donate_data"
            val lastDDDate =
                context.getString(SettingsPrefs, "last_update_dd_date", "null")
            val getDoc = Get<String>(latestUrl).await()
            JSONObject(getDoc).apply {
                val date = optString("name").takeIf { e -> e.isNotBlank() } ?: return@scopeNetLife
                if (date != lastDDDate) downloadJson(context, date)
                else loadJson(context, ddFile)
            }
        }.catch { return@catch }
    }

    private fun downloadJson(context: Context, date: String) {
        scopeNetLife {
            val file =
                Get<File>("https://raw.gitmirror.com/luckyzyx/LuckyTool_Doc/main/donate.json") {
                    setDownloadDir(ddFile)
                    setDownloadMd5Verify()
                    setDownloadTempFile()
                }.await()
            if (file.exists()) {
                loadJson(context, file)
                context.putString(SettingsPrefs, "last_update_dd_date", date)
            }
        }
    }

    private fun loadJson(context: Context, file: File) {
        scopeLife {
            if (file.readText().contains("datas")) {
                val json = base64Encode(file.readText())
                file.writeText("e$json")
            }
            allData.clear()
            val jsonObject = JSONObject(
                base64Decode(file.readText().let { it.substring(1, it.length) })
            )
            val datas = jsonObject.optJSONArray("datas") ?: JSONArray()
            var count = 0.0
            var chsCount = 0.0
            var otherCount = 0.0
            for (i in 0 until datas.length()) {
                val obj = datas.getJSONObject(i)
                val name = obj.optString("name")
                val details = obj.optJSONArray("details") ?: JSONArray()
                val infos = ArrayList<DCInfo>()
                for (o in 0 until details.length()) {
                    count++
                    val info = details.optJSONObject(o)
                    val time = info.optString("time")
                    val channel = info.optString("channel")
                    val money = info.optDouble("money")
                    val order = info.optString("order")
                    val unit = info.optString("unit")
                    infos.add(DCInfo(time, channel, money, order, unit))
                    when (unit) {
                        "RMB" -> chsCount += money
                        "$" -> otherCount += money
                    }
                }
                allData.add(DInfo(name, infos.toTypedArray()))
            }
            val develop = context.getBoolean(SettingsPrefs, "hidden_function", false)
            if (develop) allData.add(
                0,
                DInfo(
                    "$count",
                    arrayOf(
                        DCInfo("", "", DecimalFormat("0.00").format(chsCount).toDouble(), ""),
                        DCInfo("", "", DecimalFormat("0.00").format(otherCount).toDouble(), "", "$")
                    )
                )
            )
            donateAdapter = DonateListAdapter(context, allData)
            binding.recyclerView.apply {
                adapter = donateAdapter
                layoutManager = LinearLayoutManager(context)
            }
            binding.swipeRefreshLayout.isRefreshing = false
            binding.searchView.isEnabled = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init(requireActivity())
    }

    class DonateListAdapter(val context: Context, data: ArrayList<DInfo>) :
        RecyclerView.Adapter<DonateListAdapter.ViewHolder>() {

        var allDatas = ArrayList<DInfo>()
        var filterDatas = ArrayList<DInfo>()

        init {
            allDatas = data
            filterDatas = data
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                LayoutDonateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = filterDatas[position].name
            filterDatas[position].details.apply {
                var isChs = false
                var isOther = false
                var count = 0.0
                var chsCount = 0.0
                var otherCount = 0.0
                forEach {
                    when (it.unit) {
                        "RMB" -> {
                            isChs = true
                            chsCount += it.money
                        }

                        "$" -> {
                            isOther = true
                            otherCount += it.money
                        }

                        else -> count += it.money
                    }
                }
                val newline = if (isChs && isOther) "\n" else ""
                val final =
                    (if (isChs) "$chsCount RMB" else "") + newline + (if (isOther) "$otherCount $" else "") + (if (count != 0.0) "\n$count" else "")
                holder.money.text = final
            }
        }

        val getFilter
            get() = object : Filter() {
                override fun performFiltering(constraint: CharSequence): FilterResults {
                    filterDatas = if (constraint.isBlank()) allDatas
                    else {
                        val filterlist = ArrayList<DInfo>()
                        allDatas.forEach {
                            if (it.name.lowercase().contains(constraint.toString().lowercase())) {
                                filterlist.add(it)
                            }
                        }
                        filterlist
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filterDatas
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                    @Suppress("UNCHECKED_CAST")
                    filterDatas = results?.values as ArrayList<DInfo>
                    refreshDatas()
                }
            }

        override fun getItemCount(): Int = filterDatas.size

        @SuppressLint("NotifyDataSetChanged")
        fun refreshDatas() {
            notifyDataSetChanged()
        }

        class ViewHolder(binding: LayoutDonateItemBinding) : RecyclerView.ViewHolder(binding.root) {
            val name = binding.donateName
            val money = binding.donateMoney
        }
    }
}