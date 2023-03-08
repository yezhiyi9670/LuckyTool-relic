package com.luckyzyx.luckytool.hook.statusbar

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.net.TrafficStats
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.data.dp
import com.luckyzyx.luckytool.utils.tools.ModulePrefs
import java.text.DecimalFormat

object StatusBarNetWorkSpeed : YukiBaseHooker() {

    @SuppressLint("DiscouragedApi")
    override fun onHook() {
        var networkSpeed = prefs(ModulePrefs).getBoolean("set_network_speed", false)
        dataChannel.wait<Boolean>("set_network_speed") { networkSpeed = it }

        //Search postUpdateNetworkSpeedDelay
        VariousClass(
            "com.oplusos.systemui.statusbar.controller.NetworkSpeedController",
            "com.oplus.systemui.statusbar.phone.netspeed.OplusNetworkSpeedControllExImpl"
        ).hook {
            injectMember {
                method {
                    name = "postUpdateNetworkSpeedDelay"
                    paramCount = 1
                }
                beforeHook {
                    if (networkSpeed && (args().first().long() == 4000L)) {
                        args(0).set(1000L)
                    }
                }
            }
        }

        val layoutMode = prefs(ModulePrefs).getString("statusbar_network_layout", "0")
        val userTypeface =
            prefs(ModulePrefs).getBoolean("statusbar_network_user_typeface", false)
        var noSecond = prefs(ModulePrefs).getBoolean("statusbar_network_no_second", false)
        dataChannel.wait<Boolean>("statusbar_network_no_second") { noSecond = it }
        var getDoubleSize = prefs(ModulePrefs).getInt("set_network_speed_font_size", 7)
        dataChannel.wait<Int>("set_network_speed_font_size") { getDoubleSize = it }
        var getDoublePadding = prefs(ModulePrefs).getInt("set_network_speed_padding_bottom", 2)
        dataChannel.wait<Int>("set_network_speed_padding_bottom") { getDoublePadding = it }

        //Source NetworkSpeedView
        VariousClass(
            "com.oplusos.systemui.statusbar.widget.NetworkSpeedView",
            "com.oplus.systemui.statusbar.phone.netspeed.widget.NetworkSpeedView"
        ).hook {
            injectMember {
                method { name = "onFinishInflate" }
                afterHook {
                    val mView = instance<FrameLayout>()
                    val res = mView.resources
                    val mSpeedNumber =
                        field { name = "mSpeedNumber" }.get(instance).cast<TextView>()
                    val mSpeedUnit = field { name = "mSpeedUnit" }.get(instance).cast<TextView>()
                    if (userTypeface) {
                        mSpeedNumber?.typeface = Typeface.DEFAULT_BOLD
                        mSpeedUnit?.typeface = Typeface.DEFAULT_BOLD
                    }
                    when (layoutMode) {
                        "1" -> {
                            mSpeedNumber?.apply {
                                setTextSize(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    getDoubleSize.toFloat() * 2
                                )
                                layoutParams = LayoutParams(layoutParams).apply {
                                    width = LayoutParams.WRAP_CONTENT
                                    height = LayoutParams.MATCH_PARENT
                                    gravity = Gravity.CENTER
                                }
                            }
                            val speedUnitId = res.getIdentifier("unit", "id", packageName)
                            val speedUnit: TextView = speedUnitId.let { mView.findViewById(it) }
                            mView.removeView(speedUnit)
                        }
                        "2" -> {
                            mSpeedNumber?.apply {
                                setTextSize(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    getDoubleSize.toFloat()
                                )
                            }
                            mSpeedUnit?.apply {
                                setTextSize(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    getDoubleSize.toFloat()
                                )
                            }
                        }
                    }
                }
            }
            injectMember {
                method {
                    name = "updateNetworkSpeed"
                }
                beforeHook {
                    if (layoutMode == "0") return@beforeHook
                    instance<FrameLayout>().apply {
                        layoutParams?.width = LayoutParams.WRAP_CONTENT
                        setPadding(0, 0, 0, getDoublePadding.dp)
                    }
                    val speed = args().first().string().let {
                        if (noSecond) it.replace("/s", "") else it
                    }
                    val mSpeedNumber =
                        field { name = "mSpeedNumber" }.get(instance).cast<TextView>()
                    val mSpeedUnit = field { name = "mSpeedUnit" }.get(instance).cast<TextView>()

                    when (layoutMode) {
                        "1" -> mSpeedNumber?.text = speed
                        "2" -> {
                            mSpeedNumber?.text = getTotalUpSpeed().let {
                                if (noSecond) it.replace(
                                    "/s", ""
                                ) else it
                            }
                            mSpeedUnit?.text = getTotalDownloadSpeed().let {
                                if (noSecond) it.replace(
                                    "/s", ""
                                ) else it
                            }
                        }
                    }
                    instance<FrameLayout>().requestLayout()
                    resultNull()
                }
            }
        }
    }

    /** 上次总的上行流量 */
    private var mLastTotalUp: Long = 0L

    /** 上次总的下行流量 */
    private var mLastTotalDown: Long = 0L

    /** 上次总的上行时间戳 */
    private var lastTimeStampTotalUp: Long = 0L

    /** 上次总的下行时间戳 */
    private var lastTimeStampTotalDown: Long = 0L

    //获取总的上行速度
    private fun getTotalUpSpeed(): String {
        //换算后的上行速度
        val totalUpSpeed: Float
        val currentTotalTxBytes = TrafficStats.getTotalTxBytes()

        /** 当前系统时间戳 */
        val nowTimeStampTotalUp = System.currentTimeMillis()

        /** 当前总的上行流量 */
        val mCurrentTotalUp = currentTotalTxBytes - mLastTotalUp

        /** 当前总的间隔时间 */
        val mCurrentIntervals = nowTimeStampTotalUp - lastTimeStampTotalUp

        //计算上传速度
        val bytes = ((mCurrentTotalUp * 1000.0) / (mCurrentIntervals * 1.0)).toFloat()
        if (bytes.isInfinite() || bytes.isNaN()) return "0B/s"
        val unit: String
        if (bytes >= (1024 * 1024)) {
            totalUpSpeed = DecimalFormat("0.0").format(bytes / (1024 * 1024)).toFloat()
            unit = "MB/s"
        } else if (bytes >= 1024) {
            totalUpSpeed = DecimalFormat("0.0").format(bytes / 1024).toFloat()
            unit = "KB/s"
        } else {
            totalUpSpeed = DecimalFormat("0.0").format(bytes).toFloat()
            unit = "B/s"
        }
        //保存当前的流量总和和上次的时间戳
        mLastTotalUp = currentTotalTxBytes
        lastTimeStampTotalUp = nowTimeStampTotalUp
        return "" + totalUpSpeed.toString() + unit
    }

    //获取总的下行速度
    private fun getTotalDownloadSpeed(): String {
        //换算后的下行速度
        val totalDownSpeed: Float
        val currentTotalRxBytes = TrafficStats.getTotalRxBytes()

        /** 当前系统时间戳 */
        val nowTimeStampTotalDown = System.currentTimeMillis()

        /** 当前总的下行流量 */
        val mCurrentTotalDown = currentTotalRxBytes - mLastTotalDown

        /** 当前总的间隔时间 */
        val mCurrentIntervals = nowTimeStampTotalDown - lastTimeStampTotalDown

        //计算下行速度
        val bytes = ((mCurrentTotalDown * 1000.0) / (mCurrentIntervals * 1.0)).toFloat()
        if (bytes.isInfinite() || bytes.isNaN()) return "0B/s"
        val unit: String
        if (bytes >= (1024 * 1024)) {
            totalDownSpeed = DecimalFormat("0.0").format(bytes / (1024 * 1024)).toFloat()
            unit = "MB/s"
        } else if (bytes >= 1024) {
            totalDownSpeed = DecimalFormat("0.0").format(bytes / 1024).toFloat()
            unit = "KB/s"
        } else {
            totalDownSpeed = DecimalFormat("0.0").format(bytes).toFloat()
            unit = "B/s"
        }
        //保存当前的流量总和和上次的时间戳
        mLastTotalDown = currentTotalRxBytes
        lastTimeStampTotalDown = nowTimeStampTotalDown

        return "" + totalDownSpeed.toString() + unit
    }
}