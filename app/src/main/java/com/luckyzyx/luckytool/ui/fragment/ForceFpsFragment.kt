package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentFpsBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.SettingsPrefs
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.getBoolean
import com.luckyzyx.luckytool.utils.getFpsMode1
import com.luckyzyx.luckytool.utils.getFpsMode2
import com.luckyzyx.luckytool.utils.getInt
import com.luckyzyx.luckytool.utils.getRefreshRateStatus
import com.luckyzyx.luckytool.utils.putBoolean
import com.luckyzyx.luckytool.utils.putInt
import com.luckyzyx.luckytool.utils.toast

class ForceFpsFragment : Fragment() {

    private lateinit var binding: FragmentFpsBinding

    private var allData = ArrayList<ArrayList<*>>()
    private var idData = ArrayList<Int>()
    private var fpsData = ArrayList<String>()
    private var fpsAdapter: ArrayAdapter<String>? = null

    private var fpsMode = 1
    private var fpsAutostart = false
    private var currentFps = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFpsBinding.inflate(inflater)
        return binding.root
    }

    fun init(context: Context) {
        fpsMode = context.getInt(SettingsPrefs, "fps_mode", 1)
        allData = if (fpsMode == 1) getFpsMode1() else getFpsMode2()
        if (allData.size == 2) {
            allData[0].forEach { idData.add(it as Int) }
            allData[1].forEach { fpsData.add(it as String) }
        }
        currentFps = context.getInt(SettingsPrefs, "current_fps", -1)
        fpsAutostart = context.getBoolean(SettingsPrefs, "fps_autostart", false)
        fpsAdapter = ArrayAdapter(
            context, android.R.layout.simple_list_item_single_choice, fpsData
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = requireActivity()
        init(context)
        val isUnsupport = allData.isEmpty() || idData.isEmpty() || fpsData.isEmpty()
        if (isUnsupport) {
            context.toast(getString(R.string.fps_no_data))
        }
        val fpsSelfStart = binding.fpsSelfStart.apply {
            text = getString(R.string.fps_autostart)
            isChecked = fpsAutostart
            isEnabled = !isUnsupport && currentFps != -1
            setOnCheckedChangeListener { _, isChecked ->
                context.putBoolean(SettingsPrefs, "fps_autostart", isChecked)
                context.dataChannel("com.android.systemui")
                    .put("fps_autostart", isChecked)
            }
        }
        binding.fpsList.apply {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            if (!isUnsupport) adapter = fpsAdapter
            val curFpsId = idData.indexOf(currentFps)
            if (curFpsId != -1) setItemChecked(curFpsId, currentFps != -1)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                fpsSelfStart.isEnabled = true
                val cur = idData[position]
                context.putInt(SettingsPrefs, "current_fps", cur)
                requireActivity().dataChannel("com.android.systemui")
                    .put("current_fps", cur)
                if (fpsMode == 2) ShellUtils.execCommand(
                    "service call SurfaceFlinger 1035 i32 $cur", true
                )
            }
        }
        binding.fpsModeTitle.text = getString(R.string.fps_mode_title)
        binding.fpsMode1.apply {
            text = "Peak Refresh Rate"
            if (fpsMode == 1) toggle()
            setOnCheckedChangeListener { buttonview, _ ->
                if (!buttonview.isPressed) return@setOnCheckedChangeListener
                fpsSelfStart.isChecked = false
                fpsSelfStart.isEnabled = false
                context.changeFpsMode(1)
            }
        }
        binding.fpsMode2.apply {
            text = "Surfaceflinger Backdoor"
            if (fpsMode == 2) toggle()
            setOnCheckedChangeListener { buttonview, _ ->
                if (!buttonview.isPressed) return@setOnCheckedChangeListener
                fpsSelfStart.isChecked = false
                fpsSelfStart.isEnabled = false
                context.changeFpsMode(2)
            }
        }
        binding.fpsShow.apply {
            text = getString(R.string.display_refresh_rate)
            isChecked = getRefreshRateStatus()
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) ShellUtils.execCommand(
                    "service call SurfaceFlinger 1034 i32 ${if (isChecked) 1 else 0}",
                    true
                )
            }
        }
        binding.fpsRecover.apply {
            text = getString(R.string.restore_default_refresh_rate)
            isVisible = fpsMode == 2
            setOnClickListener {
                fpsSelfStart.isChecked = false
                fpsSelfStart.isEnabled = false
                context.changeFpsMode(-1)
            }
        }
        binding.fpsTips.apply {
            text = getString(R.string.fps_tips)
            gravity = Gravity.CENTER
        }
    }

    private fun Context.changeFpsMode(mode: Int) {
        if (mode != -1) {
            putInt(SettingsPrefs, "fps_mode", mode)
            dataChannel("com.android.systemui").put("fps_mode", mode)
        }
        binding.fpsList.setItemChecked(binding.fpsList.checkedItemPosition, false)
        putInt(SettingsPrefs, "current_fps", -1)
        dataChannel("com.android.systemui").put("current_fps", -1)
        ShellUtils.execCommand("service call SurfaceFlinger 1035 i32 -1", true)
        (activity as MainActivity).restart()
    }
}