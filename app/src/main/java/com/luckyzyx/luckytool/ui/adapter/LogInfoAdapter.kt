package com.luckyzyx.luckytool.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.hook.log.YukiLoggerData
import com.luckyzyx.luckytool.databinding.LayoutLoginfoItemBinding
import com.luckyzyx.luckytool.utils.data.*
import java.text.SimpleDateFormat

class LogInfoViewAdapter(val context: Context, private val data: ArrayList<YukiLoggerData>) :
    RecyclerView.Adapter<LogInfoViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutLoginfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(data[position].timestamp)
        val tag = data[position].tag
        val priority = data[position].priority
        val packageName = data[position].packageName
        val userId = data[position].userId
        val msg = data[position].msg
        val throwable = data[position].throwable.toString()
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
                    context.copyStr("[${time}]\n[${tag}][${priority}][${packageName}][${userId}]\nMessage -> \n${msg}\nThrowable -> \n${throwable}\n\n")
                }
            }.show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(binding: LayoutLoginfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val logRoot = binding.root
        val logIcon: ImageView = binding.logIcon
        val logTime: TextView = binding.logTime
        val logMsg: TextView = binding.logMsg
    }
}