package com.luckyzyx.luckytool.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.log.YukiLoggerData
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentLogsBinding
import com.luckyzyx.luckytool.ui.adapter.LogInfoViewAdapter
import com.luckyzyx.luckytool.utils.data.toast
import rikka.core.util.ResourceUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class LoggerFragment : Fragment() {

    private lateinit var binding: FragmentLogsBinding

    private var listData = ArrayList<YukiLoggerData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.loglistView.apply {
            adapter = LogInfoViewAdapter(context, listData)
            layoutManager = LinearLayoutManager(context)
        }
        if (listData.isEmpty()) loadLogger()
    }

    private fun loadLogger() {
        listData.clear()
        requireActivity().resources.getStringArray(R.array.xposed_scope).forEach { scope ->
            requireActivity().dataChannel(scope).obtainLoggerInMemoryData { its ->
                its.takeIf { e -> e.isNotEmpty() }?.forEach { e -> listData.add(e) }
                binding.loglistView.adapter?.notifyDataSetChanged()
                binding.loglistView.isVisible = listData.isNotEmpty()
                binding.logNodataView.apply {
                    text = context.getString(R.string.log_no_data)
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
            if (ResourceUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, "save").apply {
            setIcon(R.drawable.ic_baseline_save_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)) {
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) loadLogger()
        if (item.itemId == 2) {
            val fileName =
                "LuckyTool_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".log"
            saveFile(fileName)
        }
        return super.onOptionsItemSelected(item)
    }

    private val createDocument =
        registerForActivityResult(ActivityResultContracts.CreateDocument("text/log")) {
            if (it != null) {
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
            val time = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(it.timestamp)
            val messageFinal = if (it.msg != "null") "\nMessage -> \n${it.msg}" else ""
            val throwableFinal = if (it.throwable.toString() != "null") "\nThrowable -> \n${it.throwable}\n\n" else "\n\n"
            str += "[${time}]\n[${it.tag}][${it.priority}][${it.packageName}][${it.userId}]$messageFinal$throwableFinal"
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