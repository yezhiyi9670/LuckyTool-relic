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
import com.luckyzyx.luckytool.utils.data.formatDate
import com.luckyzyx.luckytool.utils.tools.NotifyUtils
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*
import kotlin.math.abs

object BatteryInfoNotify : YukiBaseHooker() {
    //battery
    private var status: String = ""
    private var plugged: String = ""
    private var level: Int = 0
    private var temperature: Double = 0.0
    private var voltage: Double = 0.0
    private var electricCurrent: Int = 0
    private var max_charging_current: Double = 0.0
    private var max_charging_voltage: Double = 0.0
    private var isCharging: Boolean = false
    private var isWireless: Boolean = false

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
            1 -> context.getString(R.string.battery_status_unknown)
            2 -> context.getString(R.string.battery_status_charging)
            3 -> context.getString(R.string.battery_status_discharging)
            4 -> context.getString(R.string.battery_status_not_charging)
            5 -> context.getString(R.string.battery_status_full)
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
        isWireless = plugged == "WIRELESS"
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        temperature = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0)
        val originalVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        voltage =
            if (originalVoltage.toString().length == 1) originalVoltage * 1.0 else originalVoltage / 1000.0
        electricCurrent = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        max_charging_current = intent.getIntExtra("max_charging_current", 0) / 1000000.0
        max_charging_voltage = intent.getIntExtra("max_charging_voltage", 0) / 1000000.0
    }

    private fun initOplusInfo(intent: Intent) {
        chargerVoltage = (intent.getIntExtra("chargervoltage", 0) / 1000.0)
        chargerTechnology = (intent.getIntExtra("chargertechnology", 0))
        chargeWattage = (intent.getIntExtra("chargewattage", 0))
        ppsMode = (intent.getIntExtra("pps_chg_mode", 0))
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            "luckytool_notify", "LuckyTool", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
        }
        NotifyUtils.createChannel(context, channel)
    }

    private fun sendNotification(context: Context, isCharging: Boolean, isUpdateTime: Boolean) {
        createChannel(context)
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
            else -> "Error: $chargerTechnology"
        }
        val chargerVoltageFinal = when (chargerTechnology) {
            //normal (pps),vooc
            0, 1 -> if (isWireless) Formatter().format("%.2f", max_charging_voltage).toString()
            else if (ppsMode == 1) chargerVoltage.toString()
            else Formatter().format("%.2f", max_charging_current).toString()
            //svooc
            2 -> if (isWireless) Formatter().format("%.2f", max_charging_voltage).toString() else Formatter().format("%.2f", max_charging_current).toString()
            20, 25, 30 -> Formatter().format("%.2f", max_charging_current).toString()
            //pd,qc
            3, 4 -> Formatter().format("%.2f", max_charging_current).toString()
            else -> 0.0.toString()
        }
        val powerCalc = when (chargerTechnology) {
            //normal,vooc,svooc
            0, 1, 2 -> if (isWireless) max_charging_current * max_charging_voltage
            else if (ppsMode == 1) chargerVoltage * electricCurrent
            else max_charging_current * electricCurrent
            //svooc
            20, 25 -> max_charging_current * electricCurrent
            //pd,qc
            3, 4 -> max_charging_current * electricCurrent
            else -> 0.0
        }
        val batteryIcon = when (level) {
            100 -> R.drawable.round_battery_full_24
            in 80..99 -> R.drawable.round_battery_6_bar_24
            in 65..79 -> R.drawable.round_battery_5_bar_24
            in 50..64 -> R.drawable.round_battery_4_bar_24
            in 35..49 -> R.drawable.round_battery_3_bar_24
            in 25..34 -> R.drawable.round_battery_2_bar_24
            in 10..24 -> R.drawable.round_battery_1_bar_24
            in 0..9 -> R.drawable.round_battery_0_bar_24
            else -> R.drawable.round_battery_unknown_24
        }
        val batteryInfo = "${context.getString(R.string.battery_temperature)}: ${temperature}℃ " +
                "${context.getString(R.string.battery_voltage)}: ${voltage}v " +
                "${context.getString(R.string.battery_electric_current)}: ${electricCurrent}mA"
        val chargeInfo = if (isCharging) {
            val power = Formatter().format("%.2f", abs(powerCalc) / 1000.0).toString()
            val wattage = if (chargeWattage != 0) "${chargeWattage}W" else ""
            "$status: $plugged ${context.getString(R.string.battery_charger_voltage)}: ${chargerVoltageFinal}v ${
                context.getString(R.string.battery_power)
            }: ${power}W\n${context.getString(R.string.battery_technology)}: $technology $wattage" + if (isUpdateTime) "\n" else ""
        } else ""
        val updateTime = if (isUpdateTime) {
            "${context.getString(R.string.battery_update_time)}: ${formatDate("HH:mm:ss")}"
        } else ""
        val notify = NotificationCompat.Builder(context, "luckytool_notify").apply {
            setAutoCancel(false)
            setOngoing(true)
            setSmallIcon(if (isCharging) R.drawable.ic_round_battery_charging_full_24 else batteryIcon)
            setContentTitle(batteryInfo)
            if (isCharging || isUpdateTime) {
                setStyle(
                    NotificationCompat.BigTextStyle().bigText("$chargeInfo$updateTime")
                )
            }
            priority = NotificationCompat.PRIORITY_DEFAULT
        }.build()
        NotifyUtils.sendNotification(context, 112233, notify)
    }

    @Suppress("unused")
    private fun clearNotification(context: Context) {
        NotifyUtils.clearNotification(context, 112233)
    }
}