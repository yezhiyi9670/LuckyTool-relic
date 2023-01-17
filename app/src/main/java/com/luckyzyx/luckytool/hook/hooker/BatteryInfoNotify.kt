package com.luckyzyx.luckytool.hook.hooker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import androidx.core.app.NotificationCompat
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.tools.NotifyUtils
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object BatteryInfoNotify : YukiBaseHooker() {
    //battery
    private var status: String = ""
    private var plugged: String = ""
    private var temperature: Double = 0.0
    private var voltage: Double = 0.0
    private var electricCurrent: Int = 0
    private var max_charging_current: Int = 0
    private var max_charging_voltage: Int = 0
    private var isCharging: Boolean = false

    //oplus battery
    private var chargerVoltage: Double = 0.0
    private var chargerTechnology: Int = 0
    private var chargeWattage: Int = 0
    private var ppsMode: Int = 0
    override fun onHook() {
        loadApp("com.android.systemui") {
            var showInfo = prefs(XposedPrefs).getBoolean("battery_information_show", false)
            dataChannel.wait<Boolean>(key = "battery_information_show") { showInfo = it }
            if (!showInfo) return
            var showCharger =
                prefs(XposedPrefs).getBoolean("battery_information_show_charge", false)
            dataChannel.wait<Boolean>(key = "battery_information_show_charge") {
                showCharger = it
            }
            var showUpdateTime =
                prefs(XposedPrefs).getBoolean("battery_information_show_update_time", false)
            dataChannel.wait<Boolean>(key = "battery_information_show_update_time") {
                showUpdateTime = it
            }
            onAppLifecycle {
                onCreate { injectModuleAppResources() }
                //监听电池信息
                registerReceiver(Intent.ACTION_BATTERY_CHANGED) { context: Context, intent: Intent ->
                    initInfo(context, intent)
                    sendNotification(context, showCharger && isCharging, showUpdateTime)
                }
                //监听OPLUS电池信息
                registerReceiver("android.intent.action.ADDITIONAL_BATTERY_CHANGED") { context: Context, intent: Intent ->
                    initOplusInfo(intent)
                    sendNotification(context, showCharger && isCharging, showUpdateTime)
                }
            }
        }
    }

    private fun initInfo(context: Context, intent: Intent) {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val statusValue = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        status = when (statusValue) {
            1 -> if (isZh(context)) "未知" else "Unknown"
            2 -> if (isZh(context)) "充电中" else "Charging"
            3 -> if (isZh(context)) "放电中" else "Discharging"
            4 -> if (isZh(context)) "未充电" else "Not Charging"
            5 -> if (isZh(context)) "已充满" else "Full"
            else -> "null"
        }
        isCharging = statusValue == 2 || statusValue == 5
        plugged = when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)) {
            0 -> "Battery"
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "DOCK"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "WIRELESS"
            else -> "null"
        }
        temperature = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0)
        val originalVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        voltage =
            if (originalVoltage.toString().length == 1) originalVoltage * 1.0 else originalVoltage / 1000.0
        electricCurrent = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        max_charging_current = intent.getIntExtra("max_charging_current", 0)
        max_charging_voltage = intent.getIntExtra("max_charging_voltage", 0)
    }

    private fun initOplusInfo(intent: Intent) {
        chargerVoltage = (intent.getIntExtra("chargervoltage", 0) / 1000.0)
        chargerTechnology = (intent.getIntExtra("chargertechnology", 0))
        chargeWattage = (intent.getIntExtra("chargewattage", 0))
        ppsMode = (intent.getIntExtra("pps_chg_mode", 0))
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            "luckytool_notify", "LuckyTool", NotificationManager.IMPORTANCE_LOW
        ).apply {
            setSound(null, null)
        }
        NotifyUtils.createChannel(context, channel)
    }

    private fun sendNotification(context: Context, isCharging: Boolean, isUpdateTime: Boolean) {
        createChannel(context)
        val batteryInfo = if (isZh(context)) {
            "温度:${temperature}℃ 电压:${voltage}v 电流:${electricCurrent}mA"
        } else "Battery Tamp: ${temperature}℃ Vol: ${voltage}v Cur: ${electricCurrent}mA"
        val technology = when (chargerTechnology) {
            0 -> if (ppsMode == 1) "PPS" else "Normal"
            1 -> "VOOC"
            2 -> "SUPERVOOC"
            20 -> "SUPERVOOC2.0"
            30 -> "SUPERVOOC Athena Foreign Pro"
            25 -> "VOOC Beta Pro"
            3 -> "PD"
            4 -> "QC"
            5 -> "PPS" //null
            6 -> "UFCS" //null
            else -> "Error"
        }
        val chargerVoltageFinal = when (chargerTechnology) {
            //normal,pps
            0 -> if (ppsMode == 1) chargerVoltage else 5.0
            //vooc,pd,qc
            1, 3, 4 -> chargerVoltage
            //svooc
            2, 20, 25, 30 -> voltage * 2
            else -> 0.0
        }
        val chargeInfo = if (isCharging) {
            val power =
                Formatter().format("%.2f", (chargerVoltageFinal * abs(electricCurrent)) / 1000.0)
                    .toString()
            val wattage = if (chargeWattage != 0) " ${chargeWattage}W" else ""
            if (isZh(context)) {
                "$status:$plugged 充电电压:${chargerVoltageFinal}v 理论功率:${power}W\n充电技术:${technology}${wattage}" + if (isUpdateTime) "\n" else ""
            } else "$status: $plugged Vol: ${chargerVoltageFinal}v Pwr: ${power}W Tech: $technology${wattage}" + if (isUpdateTime) "\n" else ""
        } else ""
        val updateTime = if (isUpdateTime) {
            if (isZh(context)) {
                "更新时间:${SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(Date())}"
            } else "UpdateTime: ${
                SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss", Locale.US
                ).format(Date())
            }"
        } else ""
        val notify = NotificationCompat.Builder(context, "luckytool_notify").apply {
            setAutoCancel(false)
            setOngoing(true)
            setSmallIcon(if (isCharging) R.drawable.ic_round_battery_charging_full_24 else R.drawable.ic_round_battery_std_24)
            setContentTitle(batteryInfo)
            if (isCharging || isUpdateTime) {
                setStyle(NotificationCompat.BigTextStyle().bigText("$chargeInfo$updateTime"))
            }
            priority = NotificationCompat.PRIORITY_MAX
        }.build()
        NotifyUtils.sendNotification(context, 112233, notify)
    }

    @Suppress("unused")
    private fun clearNotofication(context: Context) {
        NotifyUtils.clearNotification(context, 112233)
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locales[0]
        val language = locale.language
        return language.endsWith("zh")
    }
}