package com.luckyzyx.luckytool.hook.scope.systemui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import androidx.core.app.NotificationCompat
import androidx.core.os.EnvironmentCompat
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.hook.utils.IChargerUtils
import com.luckyzyx.luckytool.hook.utils.SystemPropertiesUtils
import com.luckyzyx.luckytool.utils.DevicesConfigUtils
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.NotifyUtils
import com.luckyzyx.luckytool.utils.formatDate
import com.luckyzyx.luckytool.utils.formatDouble
import com.luckyzyx.luckytool.utils.getBooleanProperty
import com.luckyzyx.luckytool.utils.getIntProperty
import com.luckyzyx.luckytool.utils.getStringProperty
import com.luckyzyx.luckytool.utils.safeOfNull
import java.io.StringReader
import java.util.Locale
import java.util.Properties
import kotlin.math.abs

object StatusBarBatteryInfoNotify : YukiBaseHooker() {
    //battery
    private var status: String = ""
    private var statusValue: Int = 0
    private var plugged: String = ""
    private var level: Int = 0
    private var level_sub: Int = 0
    private var temperature: Double = 0.0
    private var temperature_noplug: Double = 0.0
    private var voltage: Double = 0.0
    private var voltage2: Double = 0.0
    private var electricCurrent: Int = 0
    private var wirelessVol: Double = 0.0
    private var wirelessCur: Int = 0

    private var isCharging: Boolean = false
    private var isWireless: Boolean = false

    //oplus battery
    private var chargerVoltage: Int = 0
    private var chargerTechnology: Int = 0
    private var chargeWattage: Int = 0
    private var ppsMode: Int = 0

    private var isSeriesDual = false
    private var isParallelDual = false
    private var chargerType = ""

    private lateinit var chargeInfo: Properties

    //params
    private lateinit var displayMode: String
    private var showChargerInfo: Boolean = false
    private var showUpdateTime: Boolean = false
    private var isDualVol: Boolean = false
    private var isSimple: Boolean = false

    override fun onHook() {
        var thisContext: Context? = null
        displayMode = prefs(ModulePrefs).getString("battery_information_display_mode", "0")
        dataChannel.wait<String>("battery_information_display_mode") {
            displayMode = it
            initSend(thisContext)
        }
        showChargerInfo =
            prefs(ModulePrefs).getBoolean("battery_information_show_charge_info", false)
        dataChannel.wait<Boolean>("battery_information_show_charge_info") {
            showChargerInfo = it
            initSend(thisContext)
        }
        showUpdateTime =
            prefs(ModulePrefs).getBoolean("battery_information_show_update_time", false)
        dataChannel.wait<Boolean>("battery_information_show_update_time") {
            showUpdateTime = it
            initSend(thisContext)
        }
        isDualVol =
            prefs(ModulePrefs).getBoolean("battery_information_show_dual_voltage", false)
        dataChannel.wait<Boolean>("battery_information_show_dual_voltage") {
            isDualVol = it
            initSend(thisContext)
        }
        isSimple =
            prefs(ModulePrefs).getBoolean("battery_information_show_simple_mode", false)
        dataChannel.wait<Boolean>("battery_information_show_simple_mode") {
            isSimple = it
            initSend(thisContext)
        }

        onAppLifecycle {
            onCreate { injectModuleAppResources() }
            //BatteryService
            registerReceiver(Intent.ACTION_BATTERY_CHANGED) { context: Context, _: Intent ->
                thisContext = context
                context.injectModuleAppResources()
                safeOfNull { initInfo(context) }
                initSend(context)
            }
            //OplusBatteryService
            registerReceiver("android.intent.action.ADDITIONAL_BATTERY_CHANGED") { context: Context, intent: Intent ->
                thisContext = context
                context.injectModuleAppResources()
                chargerTechnology = (intent.getIntExtra("chargertechnology", 0))
                chargeWattage = (intent.getIntExtra("chargewattage", 0))
                ppsMode = (intent.getIntExtra("pps_chg_mode", 0))

                safeOfNull { initInfo(context) }
                initSend(context)
            }
        }
    }

    private fun initInfo(context: Context) {
        chargeInfo = getChargeInfo()
        statusValue = chargeInfo.getIntProperty("battery_status")
        status = when (statusValue) {
            2 -> context.getString(R.string.battery_status_charging)
            3 -> context.getString(R.string.battery_status_discharging)
            4 -> context.getString(R.string.battery_status_not_charging)
            5 -> context.getString(R.string.battery_status_full)
            else -> context.getString(R.string.battery_status_unknown)
        }
        isCharging = statusValue == 2 || statusValue == 5
        plugged = when (getPlugType(chargeInfo)) {
            0 -> "Battery"
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "WIRELESS"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "DOCK"
            else -> "Null"
        }
        isWireless = plugged == "WIRELESS"
        level = chargeInfo.getIntProperty("battery_capacity")
        level_sub = chargeInfo.getIntProperty("sub_soc")
        temperature = chargeInfo.getIntProperty("battery_temp") / 10.0
        temperature_noplug = chargeInfo.getIntProperty("battery_temp_not_plug") / 10.0
        isSeriesDual = DevicesConfigUtils.isSeriesDualBattery == true
        isParallelDual = DevicesConfigUtils.isParallelDualBattery == true
        chargerType = chargeInfo.getStringProperty("charger_type", "Null").toString()
        voltage = chargeInfo.getIntProperty("battery_voltage_now") / 1000.0
        voltage2 = if (isSeriesDual) chargeInfo.getIntProperty("battery_voltage_min") / 1000.0
        else if (isParallelDual) chargeInfo.getIntProperty("sub_voltage") / 1000.0
        else 0.0
        chargerVoltage = chargeInfo.getIntProperty("battery_charge_now")
        if (isMTKPlatformBoard == false) {
            voltage /= 1000.0
            voltage2 /= 1000.0
        }
        electricCurrent = chargeInfo.getIntProperty("battery_current_now")
        if (isWireless) {
            val isAirSVOOC = DevicesConfigUtils.isAirSVOOCSupport
            val mChargerWirelessOnline = chargeInfo.getBooleanProperty("chargerWirelessOnline")
            val mBatteryReverse = chargeInfo.getIntProperty("wireless_enable_tx")
            wirelessCur = chargeInfo.getIntProperty("wireless_current_now")
            wirelessVol = if (isAirSVOOC == true) {
                if (mChargerWirelessOnline || mBatteryReverse == 2 || mBatteryReverse == 1) {
                    chargeInfo.getIntProperty("wireless_voltage_now") / 1000.0
                } else chargerVoltage * 1.0
            } else chargeInfo.getIntProperty("wireless_voltage_now") / 1000.0
        }
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            "luckytool_notify", "LuckyTool", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
        }
        NotifyUtils.createChannel(context, channel)
    }

    private fun initSend(context: Context?) {
        if (context == null) return
        when (displayMode) {
            "1" -> sendNotification(
                context, showChargerInfo && isCharging,
                showUpdateTime, isSimple, isDualVol
            )

            "2" -> if (isCharging) sendNotification(
                context, showChargerInfo, showUpdateTime, isSimple, isDualVol
            ) else clearNotification(context)

            else -> clearNotification(context)
        }
    }

    private fun sendNotification(
        context: Context,
        isCharging: Boolean,
        isUpdateTime: Boolean,
        isSimple: Boolean,
        isDualVol: Boolean
    ) {
        createChannel(context)
        //com.oplusos.systemui.keyguard.charginganim.ChargingTypeConstants
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
        val powerCalc = if (isSeriesDual || isParallelDual) {
            (voltage + voltage2) * electricCurrent / 1000.0
        } else voltage * electricCurrent / 1000.0

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
        val power = formatDouble("%.3f", abs(powerCalc) * 1.0)
        val wattage = if (chargeWattage != 0) "${chargeWattage}W" else ""

        val tem =
            if (isSimple) "${temperature}℃" else "${context.getString(R.string.battery_temperature)}: ${temperature}℃"
        val vol = if (isSimple) {
            if (isDualVol && (isSeriesDual || isParallelDual)) "${voltage}V ${voltage2}V"
            else "${voltage}V"
        } else {
            (if (isDualVol && (isSeriesDual || isParallelDual)) "${context.getString(R.string.battery_voltage)}: ${voltage}V ${voltage2}V"
            else "${context.getString(R.string.battery_voltage)}: ${voltage}V")
        }
        val cur =
            if (isSimple) "${electricCurrent}mA" else "${context.getString(R.string.battery_electric_current)}: ${electricCurrent}mA"

        val sp = if (isSimple) "$level%" else "$status: $level%"
        val ct =
            if (isSimple) "$plugged $chargerType" else "${context.getString(R.string.battery_charger_type)}: $plugged $chargerType"
        val pwr =
            if (isSimple) "${power}W" else "${context.getString(R.string.battery_power)}: ${power}W"
        val tech = if (isSimple) "$technology $wattage" else {
            "${context.getString(R.string.battery_technology)}: $technology $wattage"
        } + if (isUpdateTime) "\n" else ""

        val wireVol =
            if (isSimple) "${wirelessVol}V" else "${context.getString(R.string.battery_voltage)}: ${wirelessVol}V"
        val wireCur =
            if (isSimple) "${wirelessCur}mA" else "${context.getString(R.string.battery_electric_current)}: ${wirelessCur}mA"
        val wirePwrCalc = formatDouble("%.3f", wirelessVol * wirelessCur / 1000.0)
        val wirePwr =
            if (isSimple) "${wirePwrCalc}W" else "${context.getString(R.string.battery_power)}: ${wirePwrCalc}W"

        val batteryInfo = if (isSimple) "$tem $vol $cur ${power}W" else "$tem $vol $cur"
        val chargeInfo = if (isCharging) {
            if (isSimple) {
                if (isWireless) "$wireVol $wireCur $wirePwr\n$sp $tech" else "$sp $ct $tech"
            } else {
                if (statusValue == 5) {
                    if (isWireless) "$sp $tech" else "$sp $tech"
                } else {
                    if (isWireless) "$wireVol $wireCur $wirePwr\n$sp $tech" else "$sp $ct $pwr\n$tech"
                }
            }
        } else ""
        val updateTime = if (isUpdateTime) {
            if (isSimple) formatDate("HH:mm:ss") else "${context.getString(R.string.battery_update_time)}: " +
                    formatDate("HH:mm:ss")
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

    private fun clearNotification(context: Context) {
        NotifyUtils.clearNotification(context, 112233)
    }

    private fun getChargeInfo(): Properties {
        val queryChargeInfo = IChargerUtils(appClassLoader).queryChargeInfo()
        return Properties().apply {
            load(StringReader(queryChargeInfo))
        }
    }

    private val isMTKPlatformBoard
        get() = getMTKSystemProp("ro.board.platform", EnvironmentCompat.MEDIA_UNKNOWN)
            ?.lowercase(Locale.US)?.startsWith("mt")

    @Suppress("SameParameterValue")
    private fun getMTKSystemProp(key: String, def: String): String? {
        val value = SystemPropertiesUtils(appClassLoader).get(key, def)
        return if (value?.isBlank() == true) def else value
    }

    private fun getPlugType(properties: Properties): Int {
        if (properties.getBooleanProperty("chargerAcOnline")) {
            return 1
        }
        if (properties.getBooleanProperty("chargerUSBOnline")) {
            return 2
        }
        if (properties.getBooleanProperty("chargerWirelessOnline")) {
            return 4
        }
        return 0
    }
}