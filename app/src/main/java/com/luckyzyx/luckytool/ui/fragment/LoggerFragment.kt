package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.log.YukiLoggerData
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentLogsBinding
import com.luckyzyx.luckytool.databinding.LayoutLoginfoItemBinding
import com.luckyzyx.luckytool.utils.data.*
import rikka.core.util.ResourceUtils.isNightMode
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class LoggerFragment : Fragment() {

    private lateinit var binding: FragmentLogsBinding
    private var listData = ArrayList<YukiLoggerData>()
    private var logInfoViewAdapter: LogInfoViewAdapter? = null
    private var fileName: String = ""
    private var filterString = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        logInfoViewAdapter = LogInfoViewAdapter(requireActivity(), listData)
        binding.loglistView.apply {
            adapter = logInfoViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        if (listData.isEmpty()) loadLogger()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadLogger() {
        listData.clear()
        requireActivity().resources.getStringArray(R.array.xposed_scope).forEach { scope ->
            requireActivity().dataChannel(scope).obtainLoggerInMemoryData { its ->
                its.takeIf { e -> e.isNotEmpty() }?.forEach { e -> listData.add(e) }
                binding.loglistView.adapter?.notifyDataSetChanged()
                binding.loglistView.isVisible = listData.isNotEmpty()
                binding.logNodataView.apply {
                    text = getString(R.string.log_no_data)
                    isVisible = listData.isEmpty()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadLogger()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, "refresh").apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, "save").apply {
            setIcon(R.drawable.ic_baseline_save_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 3, 0, "filter").apply {
            setIcon(R.drawable.baseline_filter_list_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) loadLogger()
        if (item.itemId == 2) {
            fileName = "LuckyTool_" + formatDate("yyyyMMdd_HHmmss") + ".log"
            saveFile(fileName)
        }
        if (item.itemId == 3) {
            val dialog = MaterialAlertDialogBuilder(requireActivity(), dialogCentered).apply {
                setTitle("输入要过滤的内容")
                setView(R.layout.layout_log_filter_dialog)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    logInfoViewAdapter?.getFilter?.filter(filterString)
                }
                setNeutralButton(android.R.string.cancel, null)
            }.show()
            dialog.findViewById<TextInputEditText>(R.id.log_filter)?.apply {
                setText(filterString)
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        filterString = s.toString()
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val createDocument =
        registerForActivityResult(ActivityResultContracts.CreateDocument("text/log")) {
            if (it != null) {
//                val dir = it.path?.split(":")?.get(1) ?: "/sdcard/Download/$fileName"
//                YukiHookLogger.saveToFile(dir,listData)
                alterDocument(requireActivity(), it)
            }
        }

    private fun saveFile(fileName: String) {
        checkDirs()
        if (listData.isEmpty()) {
            requireActivity().toast(getString(R.string.log_data_is_empty))
        } else {
            createDocument.launch(fileName)
        }
    }

    private fun checkDirs(): String {
        val dir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/LuckyTool")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.name
    }

    private fun alterDocument(context: Context, uri: Uri) {
        var str = ""
        listData.forEach {
            val time = formatDate("yyyy/MM/dd-HH:mm:ss", it.timestamp)
            val messageFinal = if (it.msg != "null") "\nMessage -> ${it.msg}" else ""
            val throwableFinal =
                if (it.throwable.toString() != "null") "\nThrowable -> ${it.throwable}\n\n" else "\n\n"
            str += "[${time}][${it.tag}][${it.priority}][${it.packageName}][${it.userId}]$messageFinal$throwableFinal"
        }
        try {
            context.contentResolver.openFileDescriptor(uri, "w")?.use { its ->
                FileOutputStream(its.fileDescriptor).use {
                    it.write(str.toByteArray())
                }
            }
            context.toast(getString(R.string.log_save_success))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            context.toast(getString(R.string.log_save_failed))
        } catch (e: IOException) {
            e.printStackTrace()
            context.toast(getString(R.string.log_save_failed))
        }
    }
}

class LogInfoViewAdapter(val context: Context, data: ArrayList<YukiLoggerData>) :
    RecyclerView.Adapter<LogInfoViewAdapter.ViewHolder>() {

    private var allData = ArrayList<YukiLoggerData>()
    private var filterData = ArrayList<YukiLoggerData>()

    init {
        allData = data
        filterData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutLoginfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time = formatDate("yyyy/MM/dd-HH:mm:ss", filterData[position].timestamp)
        val tag = filterData[position].tag
        val priority = filterData[position].priority
        val packageName = filterData[position].packageName
        val userId = filterData[position].userId
        val msg = filterData[position].msg
        val throwable = filterData[position].throwable.toString()
        holder.logIcon.setImageDrawable(context.getAppIcon(packageName))
        holder.logTime.text = time
        holder.logMsg.text = msg
        holder.logRoot.setOnClickListener(null)
        holder.logRoot.setOnClickListener {
            MaterialAlertDialogBuilder(context, dialogCentered).apply {
                setTitle(context.getAppLabel(packageName))
                setView(
                    NestedScrollView(context).apply {
                        addView(
                            MaterialTextView(context).apply {
                                setPadding(20.dp, 0, 20.dp, 20.dp)
                                text = msg + if (throwable != "null") "\n\n$throwable" else ""
                            }
                        )
                    }
                )
                setPositiveButton(android.R.string.copy) { _, _ ->
                    val messageFinal = if (msg != "null") "\nMessage -> $msg" else ""
                    val throwableFinal =
                        if (throwable != "null") "\nThrowable -> ${throwable}\n\n" else "\n\n"
                    context.copyStr("[${time}][${tag}][${priority}][${packageName}][${userId}]$messageFinal$throwableFinal")
                }
            }.show()
        }
    }

    override fun getItemCount(): Int {
        return filterData.size
    }

    val getFilter
        get() = object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                filterData = if (constraint.isBlank()) {
                    allData
                } else {
                    val filterlist = ArrayList<YukiLoggerData>()
                    allData.forEach {
                        if (it.toString().lowercase().contains(constraint.toString().lowercase())) {
                            filterlist.add(it)
                        }
                    }
                    filterlist
                }
                val filterResults = FilterResults()
                filterResults.values = filterData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filterData = results?.values as ArrayList<YukiLoggerData>
                refreshDatas()
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshDatas() {
        notifyDataSetChanged()
    }

    class ViewHolder(binding: LayoutLoginfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val logRoot = binding.root
        val logIcon: ImageView = binding.logIcon
        val logTime: TextView = binding.logTime
        val logMsg: TextView = binding.logMsg
    }
}