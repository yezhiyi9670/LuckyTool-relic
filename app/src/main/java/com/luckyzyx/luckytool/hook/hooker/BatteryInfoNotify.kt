package com.luckyzyx.luckytool.hook.hooker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import androidx.core.app.NotificationCompat
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.NotifyUtils
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object BatteryInfoNotify : YukiBaseHooker() {
    //battery
    private var plugged: String = ""
    private var temperature: Double = 0.0
    private var voltage: Double = 0.0
    private var electricCurrent: Int = 0
    private var max_charging_current: Int = 0
    private var max_charging_voltage: Int = 0
    //oplus battery
    private var chargerVoltage: Double = 0.0
    private var chargerTechnology: Int = 0
    private var chargeWattage: Int = 0
    private var ppsMode: Int = 0
    override fun onHook() {
        loadApp("com.android.systemui") {
            var batteryInfoShow = prefs(XposedPrefs).getBoolean("battery_information_show", false)
            dataChannel.wait<Boolean>(key = "battery_information_show") { batteryInfoShow = it }
            var batteryInfoShowCharge =
                prefs(XposedPrefs).getBoolean("battery_information_show_charge", false)
            dataChannel.wait<Boolean>(key = "battery_information_show_charge") {
                batteryInfoShowCharge = it
            }
            var batteryInfoShowUpdateTime =
                prefs(XposedPrefs).getBoolean("battery_information_show_update_time", false)
            dataChannel.wait<Boolean>(key = "battery_information_show_update_time") {
                batteryInfoShowUpdateTime = it
            }
            var isChargeStatus = false
            onAppLifecycle {
                //监听电池信息
                registerReceiver(Intent.ACTION_BATTERY_CHANGED) { context: Context, intent: Intent ->
                    initInfo(context, intent)
                    sendNotification(context, batteryInfoShow, batteryInfoShowCharge && isChargeStatus, batteryInfoShowUpdateTime)
                }
                //监听OPLUS电池信息
                registerReceiver("android.intent.action.ADDITIONAL_BATTERY_CHANGED") { context: Context, intent: Intent ->
                    initOplusInfo(intent)
                    sendNotification(context, batteryInfoShow, batteryInfoShowCharge && isChargeStatus, batteryInfoShowUpdateTime)
                }
                //监听连接充电器
                registerReceiver(Intent.ACTION_POWER_CONNECTED) { _: Context, _: Intent ->
                    isChargeStatus = true
                }
                //监听断开充电器
                registerReceiver(Intent.ACTION_POWER_DISCONNECTED) { _: Context, _: Intent ->
                    isChargeStatus = false
                }
            }
        }
    }

    private fun initInfo(context: Context, intent: Intent) {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        plugged = when (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)) {
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "DOCK"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "WIRELESS"
            else -> "null"
        }
        temperature = (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0)
        val originalVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        voltage = if (originalVoltage.toString().length == 1) originalVoltage * 1.0 else originalVoltage / 1000.0
        electricCurrent = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        max_charging_current = intent.getIntExtra("max_charging_current", 0)
        max_charging_voltage = intent.getIntExtra("max_charging_voltage", 0)
    }

    private fun initOplusInfo(intent: Intent) {
        chargerVoltage = (intent.getIntExtra("chargervoltage", 0) / 1000.0)
        chargerTechnology = (intent.getIntExtra("chargertechnology",0))
        chargeWattage = (intent.getIntExtra("chargewattage",0))
        ppsMode = (intent.getIntExtra("pps_chg_mode",0))
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            "luckytool_notify",
            "LuckyTool",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setSound(null, null)
        }
        NotifyUtils.createChannel(context, channel)
    }

    private fun sendNotification(context: Context, isShow: Boolean, isCharge: Boolean, isUpdateTime: Boolean) {
        if (!isShow) {
            clearNotofication(context)
            return
        }
        createChannel(context)
        val defaultInfo = if (isZh(context)) {
            "温度:${temperature}℃ 电压:${voltage}v 电流:${electricCurrent}mA"
        } else "${temperature}℃ ${voltage}v ${electricCurrent}mA"
        val technology = when(chargerTechnology){
            0 -> if (ppsMode == 1) "PPS" else "Normal"
            1 -> "Vooc"
            2 -> "SuperVooc"
            20 -> "SuperVooc2"
            30 -> "SuperVooc Athena Foreign Pro"
            25 -> "Vooc Beta Pro"
            3 -> "PD"
            4 -> "QC"
            5 -> "PPS"
            6 -> "UFCS"
            else -> "Error"
        }
        val chargerVoltageFinal = when (chargerTechnology) {
            //normal,pps
            0 -> if (max_charging_current == 0 && max_charging_voltage == 0) chargerVoltage else 5.0
            //vooc,pd,qc
            1, 3, 4 -> chargerVoltage
            //svooc
            2, 20, 25, 30 -> voltage * 2
            else -> 0.0
        }
        val chargeInfo = if (isCharge) {
            val power = Formatter().format("%.2f", (chargerVoltageFinal * abs(electricCurrent)) / 1000.0).toString()
            val wattage = if (chargeWattage != 0) " ${chargeWattage}W" else ""
            if (isZh(context)) {
                "充电中:$plugged 充电电压:${chargerVoltageFinal}v 理论功率:${power}W\n充电技术:${technology}${wattage}" + if (isUpdateTime) "\n" else ""
            } else "$plugged ${chargerVoltageFinal}v ${power}W $technology${wattage}" + if (isUpdateTime) "\n" else ""
        } else ""
        val updateTime = if (isUpdateTime) {
            if (isZh(context)) {
                "更新时间:${SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())}"
            } else SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Date())
        } else ""
        val notify = NotificationCompat.Builder(context, "luckytool_notify").apply {
            setAutoCancel(false)
            setOngoing(true)
            setSmallIcon(android.R.mipmap.sym_def_app_icon)
            setContentTitle(defaultInfo)
            if (isCharge || isUpdateTime) {
                setStyle(NotificationCompat.BigTextStyle().bigText("$chargeInfo$updateTime"))
            }
            priority = NotificationCompat.PRIORITY_MAX
        }.build()
        NotifyUtils.sendNotification(context, 112233, notify)
    }

    private fun clearNotofication(context: Context) {
        NotifyUtils.clearNotification(context, 112233)
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locales[0]
        val language = locale.language
        return language.endsWith("zh")
    }
}