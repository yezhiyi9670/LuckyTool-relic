package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.ArrayMap
import android.util.ArraySet
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withIO
import com.google.android.material.materialswitch.MaterialSwitch
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentApplistFunctionLayoutBinding
import com.luckyzyx.luckytool.databinding.LayoutAppinfoSwitchItemDarkmodeBinding
import com.luckyzyx.luckytool.utils.*
import rikka.core.util.ResourceUtils

class DarkModeFragment : Fragment() {

    private lateinit var binding: FragmentApplistFunctionLayoutBinding
    private var appListAllDatas = ArrayList<AppInfo>()
    private var darkModeAdapter: DarkModeAdapter? = null
    private val scopes = arrayOf("com.android.settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentApplistFunctionLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enableSwitch.apply {
            text = context.getString(R.string.enable_zoom_window)
            isChecked = context.getBoolean(ModulePrefs, "dark_mode_list_enable", false)
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    context.putBoolean(ModulePrefs, "dark_mode_list_enable", isChecked)
                    requireActivity().dataChannel("android").put("dark_mode_list_enable", isChecked)
                }
            }
        }

        binding.searchViewLayout.apply {
            hint = "Name / PackageName"
            isHintEnabled = true
            isHintAnimationEnabled = true
        }
        binding.searchView.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    darkModeAdapter?.getFilter?.filter(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                loadData()
            }
        }

        if (appListAllDatas.isEmpty()) loadData()
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        binding.swipeRefreshLayout.isRefreshing = true
        binding.searchViewLayout.isEnabled = false
        binding.searchView.text = null
        appListAllDatas.clear()
        val enableData =
            requireActivity().getStringSet(ModulePrefs, "dark_mode_support_list", ArraySet())
        val formatEnableData = ArrayMap<String, Int>()
        enableData?.forEach {
            if (it.contains("|")) {
                val arr = it.split("|").toMutableList()
                if (arr.size < 2 || arr[1].isBlank()) arr[1] = (0).toString()
                formatEnableData[arr[0]] = arr[1].toInt()
            } else formatEnableData[it] = 0
        }
        scopeLife {
            withIO {
                val packageManager = requireActivity().packageManager
                val appinfos = PackageUtils(packageManager).getInstalledApplications(0)
                for (i in appinfos) {
                    if (i.flags and ApplicationInfo.FLAG_SYSTEM == 1) continue
                    appListAllDatas.add(
                        AppInfo(
                            i.loadIcon(packageManager),
                            i.loadLabel(packageManager),
                            i.packageName,
                        )
                    )
                }
            }
            darkModeAdapter = DarkModeAdapter(requireActivity(), appListAllDatas, formatEnableData)
            binding.recyclerView.apply {
                adapter = darkModeAdapter
                layoutManager = LinearLayoutManager(context)
            }
            binding.swipeRefreshLayout.isRefreshing = false
            binding.searchViewLayout.isEnabled = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) requireActivity().restartScopes(scopes)
        return super.onOptionsItemSelected(item)
    }
}

class DarkModeAdapter(
    val context: Context,
    datas: ArrayList<AppInfo>,
    enableData: ArrayMap<String, Int>
) :
    RecyclerView.Adapter<DarkModeAdapter.ViewHolder>() {

    private var allDatas = ArrayList<AppInfo>()
    private var filterDatas = ArrayList<AppInfo>()
    private var enabledMulti = ArrayMap<String, Int>()
    private var tempData = ArrayMap<String, Int>()

    init {
        allDatas = datas
        enabledMulti = enableData
        sortDatas()
        filterDatas = datas
    }

    private fun sortDatas() {
        //检查清理已卸载APP
        allDatas.forEach { its ->
            enabledMulti.forEach {
                if (it.key == its.packName) tempData[it.key] = it.value
            }
        }
        enabledMulti.clear()
        enabledMulti = tempData
        //保存数据
        saveEnableList()
        //过滤置顶
        val tempInfo = ArrayList<AppInfo>()
        tempData.forEach { map ->
            allDatas.forEach {
                if (it.packName == map.key) tempInfo.add(it)
            }
        }
        tempInfo.forEach {
            allDatas.remove(it)
            allDatas.add(0, it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutAppinfoSwitchItemDarkmodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.appIcon.setImageDrawable(filterDatas[position].appIcon)
        holder.appName.text = filterDatas[position].appName
        holder.packName.text = filterDatas[position].packName
        holder.appInfoView.setOnClickListener(null)
        holder.switchview.setOnCheckedChangeListener(null)
        holder.radioList.setOnCheckedChangeListener(null)
        holder.radioList.clearCheck()

        holder.switchview.isChecked = enabledMulti.contains(filterDatas[position].packName)
        holder.radioList.isVisible = holder.switchview.isChecked
        holder.radioList.check(holder.radioNone.id)
        enabledMulti.forEach {
            if (it.key == filterDatas[position].packName) {
                val radioId = when (it.value) {
                    1 -> holder.radio1.id
                    2 -> holder.radio2.id
                    3 -> holder.radio3.id
                    4 -> holder.radio4.id
                    else -> holder.radioNone.id
                }
                holder.radioList.check(radioId)
            }
        }
        holder.appInfoView.setOnClickListener {
            holder.switchview.isChecked = !holder.switchview.isChecked
        }
        holder.switchview.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.radioList.check(holder.radioNone.id)
                enabledMulti[filterDatas[position].packName] = 0
                holder.radioList.isVisible = true
            } else {
                enabledMulti.remove(filterDatas[position].packName)
                holder.radioList.isVisible = false
            }
            saveEnableList()
        }
        holder.radioList.setOnCheckedChangeListener { group, checkedId ->
            val value = group.indexOfChild(group.findViewById(checkedId))
            enabledMulti[filterDatas[position].packName] = value
            saveEnableList()
        }
    }

    override fun getItemCount(): Int {
        return filterDatas.size
    }

    val getFilter
        get() = object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                filterDatas = if (constraint.isBlank()) {
                    allDatas
                } else {
                    val filterlist = ArrayList<AppInfo>()
                    allDatas.forEach {
                        if (
                            it.appName.toString().lowercase()
                                .contains(constraint.toString().lowercase()) ||
                            it.packName.lowercase().contains(constraint.toString().lowercase())
                        ) {
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
                filterDatas = results?.values as ArrayList<AppInfo>
                refreshDatas()
            }
        }

    private fun saveEnableList() {
        val data = ArraySet<String>()
        enabledMulti.forEach {
            data.add("${it.key}|${it.value}")
        }
        context.putStringSet(ModulePrefs, "dark_mode_support_list", data.toSet())
        context.dataChannel("android").put("dark_mode_support_list", data.toSet())
        context.dataChannel("com.android.settings")
            .put("dark_mode_support_list", data.toSet())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshDatas() {
        notifyDataSetChanged()
    }

    class ViewHolder(binding: LayoutAppinfoSwitchItemDarkmodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val appInfoView: ConstraintLayout = binding.appinfoView
        val appIcon: ImageView = binding.appIcon
        val appName: TextView = binding.appName
        val packName: TextView = binding.packName
        val switchview: MaterialSwitch = binding.switchview
        val radioList: RadioGroup = binding.radioList
        val radioNone: RadioButton = binding.curTypeNone
        val radio1: RadioButton = binding.curType1
        val radio2: RadioButton = binding.curType2
        val radio3: RadioButton = binding.curType3
        val radio4: RadioButton = binding.curType4
    }
}