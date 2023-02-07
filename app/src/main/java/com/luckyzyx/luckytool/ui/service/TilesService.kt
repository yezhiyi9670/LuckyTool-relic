package com.luckyzyx.luckytool.ui.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.telephony.SubscriptionManager
import com.luckyzyx.luckytool.IFiveGController
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.data.checkPackName
import com.luckyzyx.luckytool.utils.data.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.data.jumpRunningApp
import com.luckyzyx.luckytool.utils.data.toast
import com.luckyzyx.luckytool.utils.tools.SettingsPrefs
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.putBoolean
import com.topjohnwu.superuser.ipc.RootService

class ChargingTest : TileService() {
    override fun onClick() {
        jumpBatteryInfo(applicationContext)
    }
}

class ProcessManager : TileService() {
    override fun onClick() {
        jumpRunningApp(applicationContext)
    }
}

class GameAssistant : TileService() {
    override fun onStartListening() {
        if (!checkPackName("com.oplus.games")) qsTile.state =
            Tile.STATE_UNAVAILABLE else qsTile.state = Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> ShellUtils.execCommand(
                "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true
            )
            Tile.STATE_UNAVAILABLE -> toast(getString(R.string.game_assistant_tile_tips))
        }
    }
}

class ShowFPS : TileService() {
    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                showFPS(true)
                qsTile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                showFPS(false)
                qsTile.state = Tile.STATE_INACTIVE
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }

    private fun showFPS(showFPS: Boolean) {
        ShellUtils.execCommand(
            "service call SurfaceFlinger 1034 i32 ${if (showFPS) 1 else 0}", true
        )
    }
}

class HighBrightness : TileService() {
    override fun onStartListening() {
        ShellUtils.execCommand("cat /sys/kernel/oplus_display/hbm", true, true).apply {
            if (result == 1) qsTile.state = Tile.STATE_UNAVAILABLE else when (successMsg) {
                "0" -> qsTile.state = Tile.STATE_INACTIVE
                "1" -> qsTile.state = Tile.STATE_ACTIVE
            }
            qsTile.updateTile()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 1", true)
                putBoolean(SettingsPrefs, "high_brightness_mode", true)
                qsTile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 0", true)
                putBoolean(SettingsPrefs, "high_brightness_mode", false)
                qsTile.state = Tile.STATE_INACTIVE
                toast(getString(R.string.tile_close_autoself_tip))
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

class GlobalDC : TileService() {
    override fun onStartListening() {
        var oppoExist = true
        var oplusExist = true
        var isOppo = false
        var isOplus = false
        ShellUtils.execCommand("cat /sys/kernel/oppo_display/dimlayer_hbm", true, true).apply {
            if (result == 0 && successMsg == "1") isOppo = true else if (result == 1) oppoExist =
                false
        }
        ShellUtils.execCommand("cat /sys/kernel/oplus_display/dimlayer_hbm", true, true).apply {
            if (result == 0 && successMsg == "1") isOplus = true else if (result == 1) oplusExist =
                false
        }
        qsTile.state =
            if (!(oppoExist || oplusExist)) Tile.STATE_UNAVAILABLE else if (isOppo || isOplus) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oppo_display/dimlayer_hbm 1", true)
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 1", true)
                putBoolean(SettingsPrefs, "global_dc_mode", true)
                qsTile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oppo_display/dimlayer_hbm 0", true)
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 0", true)
                putBoolean(SettingsPrefs, "global_dc_mode", false)
                qsTile.state = Tile.STATE_INACTIVE
                toast(getString(R.string.tile_close_autoself_tip))
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

class TouchSamplingRate : TileService() {
    override fun onStartListening() {
        ShellUtils.execCommand("cat /proc/touchpanel/game_switch_enable", true, true).apply {
            if (result == 1) qsTile.state = Tile.STATE_UNAVAILABLE
            else if (successMsg.isBlank()) qsTile.state = Tile.STATE_UNAVAILABLE
            else when (successMsg.substring(0, 1)) {
                "0" -> qsTile.state = Tile.STATE_INACTIVE
                "1" -> qsTile.state = Tile.STATE_ACTIVE
                else -> qsTile.state = Tile.STATE_UNAVAILABLE
            }
            qsTile.updateTile()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true)
                putBoolean(SettingsPrefs, "touch_sampling_rate", true)
                qsTile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 0", true)
                putBoolean(SettingsPrefs, "touch_sampling_rate", false)
                qsTile.state = Tile.STATE_INACTIVE
                toast(getString(R.string.tile_close_autoself_tip))
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

class FiveG : TileService() {
    private var iFiveGController: IFiveGController? = null
    override fun onStartListening() {
        refreshData()
    }

    override fun onClick() = startFiveGController {
        val subId = SubscriptionManager.getDefaultDataSubscriptionId()
        if (qsTile.state == Tile.STATE_INACTIVE) {
            it?.setFiveGStatus(subId, true)
        } else if (qsTile.state == Tile.STATE_ACTIVE) {
            it?.setFiveGStatus(subId, false)
        }
        refreshData()
    }

    private fun startFiveGController(controller: (IFiveGController?) -> Unit) {
        if (iFiveGController != null) {
            controller(iFiveGController)
        } else {
            RootService.bind(
                Intent(this, FiveGControllerService::class.java), object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        iFiveGController = IFiveGController.Stub.asInterface(service)
                        controller(iFiveGController)
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {

                    }
                }
            )
        }
    }

    private fun refreshData() = startFiveGController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            qsTile.updateTile()
            return@startFiveGController
        }
        val subId = SubscriptionManager.getDefaultDataSubscriptionId()
        qsTile.state = if (!it.checkCompatibility(subId)) {
            Tile.STATE_UNAVAILABLE
        } else {
            if (it.getFiveGStatus(subId)) {
                Tile.STATE_ACTIVE
            } else {
                Tile.STATE_INACTIVE
            }
        }
        qsTile.updateTile()
    }
}