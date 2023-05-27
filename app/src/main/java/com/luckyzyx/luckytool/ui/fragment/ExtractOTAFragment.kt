package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.database.getStringOrNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withDefault
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentExtractOtaBinding
import com.luckyzyx.luckytool.utils.SQLiteUtils
import com.luckyzyx.luckytool.utils.SQLiteUtils.getTableData
import com.luckyzyx.luckytool.utils.SQLiteUtils.readOnly
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.copyStr
import com.luckyzyx.luckytool.utils.formatDataSize
import java.io.File

class ExtractOTAFragment : Fragment() {

    private lateinit var binding: FragmentExtractOtaBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExtractOtaBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SdCardPath")
    fun init(context: Context) {
        scopeLife {
            binding.swipeRefreshLayout.apply {
                setOnRefreshListener { init(context) }
                isRefreshing = true
            }
            binding.otaData.isVisible = false
            binding.noOtaData.isVisible = true
            binding.copyOtaData.isEnabled = false
            val data = withDefault {
                val command = arrayOf(
                    "mkdir -p /sdcard/Download/LuckyTool/cache/",
                    "cp /data/user/0/com.oplus.ota/databases/ota.db /sdcard/Download/LuckyTool/cache/ota.db"
                )
                ShellUtils.execCommand(command, true)
                val dbFile = File("/sdcard/Download/LuckyTool/cache/ota.db")
                val table = if (dbFile.exists()) {
                    val db = SQLiteUtils.openDataBase(dbFile.path, readOnly)
                    db?.getTableData("pkgList")
                } else null
                val dataList = ArrayList<String>()
                if (table != null) {
                    for (data in 0 until table.count) {
                        table.moveToPosition(data)
                        val packNameColumn = table.getColumnIndex("package_name")
                        val packName = table.getStringOrNull(packNameColumn) ?: "Null"
                        val sizeColumn = table.getColumnIndex("size")
                        val size = table.getStringOrNull(sizeColumn) ?: "Null"
                        val md5Column = table.getColumnIndex("md5")
                        val md5 = table.getStringOrNull(md5Column) ?: "Null"
                        val urlColumn = table.getColumnIndex("url")
                        val url = table.getStringOrNull(urlColumn) ?: "Null"
                        val activeUrlColumn = table.getColumnIndex("active_url")
                        val activeUrl = table.getStringOrNull(activeUrlColumn) ?: "Null"
                        dataList.add(
                            """
                            PackName -> $packName
                            
                            Size -> ${formatDataSize(size)}
                            
                            ActiveUrl -> $activeUrl
                            
                            Url -> $url
                            
                            MD5 -> $md5
                            
                            ${getString(R.string.extract_ota_source)} -> @LuckyTool
                            """.trimIndent()
                        )
                    }
                }
                dataList
            }
            if (data.isNotEmpty()) {
                binding.otaData.apply {
                    var str = ""
                    data.forEach { str += it + "\n\n" }
                    text = str
                    isVisible = true
                }
                binding.noOtaData.isVisible = !binding.otaData.isVisible
                binding.copyOtaData.isEnabled = binding.otaData.isVisible
            }

            binding.copyOtaData.setOnClickListener {
                requireActivity().copyStr(binding.otaData.text as String)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init(requireActivity())
    }
}