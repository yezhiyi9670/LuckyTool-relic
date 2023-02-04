package com.luckyzyx.luckytool.ui.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.data.checkPackName
import com.luckyzyx.luckytool.utils.data.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.data.jumpRunningApp
import com.luckyzyx.luckytool.utils.data.toast
import com.luckyzyx.luckytool.utils.tools.SettingsPrefs
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.putBoolean

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
        val tile = qsTile
        if (!applicationContext.checkPackName("com.oplus.games")) tile.state =
            Tile.STATE_UNAVAILABLE else tile.state = Tile.STATE_INACTIVE
        tile.updateTile()
    }

    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> ShellUtils.execCommand(
                "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity",
                true
            )
            Tile.STATE_UNAVAILABLE -> toast(getString(R.string.game_assistant_tile_tips))
        }
    }
}

class ShowFPS : TileService() {
    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> {
                showFPS(true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                showFPS(false)
                tile.state = Tile.STATE_INACTIVE
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        tile.updateTile()
    }

    private fun showFPS(showFPS: Boolean) {
        ShellUtils.execCommand(
            "service call SurfaceFlinger 1034 i32 ${if (showFPS) 1 else 0}",
            true
        )
    }
}

class HighBrightness : TileService() {
    override fun onStartListening() {
        val tile = qsTile
        ShellUtils.execCommand("cat /sys/kernel/oplus_display/hbm", true, true).apply {
            if (result == 1) tile.state = Tile.STATE_UNAVAILABLE else when (successMsg) {
                "0" -> tile.state = Tile.STATE_INACTIVE
                "1" -> {
                    tile.state = Tile.STATE_ACTIVE
                    putBoolean(SettingsPrefs, "high_brightness_mode", true)
                }
            }
            tile.updateTile()
        }
    }

    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 1", true)
                putBoolean(SettingsPrefs, "high_brightness_mode", true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 0", true)
                putBoolean(SettingsPrefs, "high_brightness_mode", false)
                tile.state = Tile.STATE_INACTIVE
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        tile.updateTile()
    }
}

class GlobalDC : TileService() {
    override fun onStartListening() {
        val tile = qsTile
        ShellUtils.execCommand("cat /sys/kernel/oplus_display/dimlayer_hbm", true, true).apply {
            if (result == 1) tile.state = Tile.STATE_UNAVAILABLE else when (successMsg) {
                "0" -> tile.state = Tile.STATE_INACTIVE
                "1" -> {
                    tile.state = Tile.STATE_ACTIVE
                    putBoolean(SettingsPrefs, "global_dc_mode", true)
                }
            }
            tile.updateTile()
        }
    }

    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 1", true)
                putBoolean(SettingsPrefs, "global_dc_mode", true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 0", true)
                putBoolean(SettingsPrefs, "global_dc_mode", false)
                tile.state = Tile.STATE_INACTIVE
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        tile.updateTile()
    }
}

class TouchSamplingRate : TileService() {
    override fun onStartListening() {
        val tile = qsTile
        ShellUtils.execCommand("cat /proc/touchpanel/game_switch_enable", true, true).apply {
            if (result == 1) tile.state = Tile.STATE_UNAVAILABLE else when (successMsg.substring(
                0,
                1
            )) {
                "0" -> tile.state = Tile.STATE_INACTIVE
                "1" -> {
                    tile.state = Tile.STATE_ACTIVE
                    putBoolean(SettingsPrefs, "touch_sampling_rate", true)
                }
            }
            tile.updateTile()
        }
    }

    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true)
                putBoolean(SettingsPrefs, "touch_sampling_rate", true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 0", true)
                putBoolean(SettingsPrefs, "touch_sampling_rate", false)
                tile.state = Tile.STATE_INACTIVE
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        tile.updateTile()
    }
}

class FiveG : TileService() {
    override fun onStartListening() {

    }

    override fun onClick() {

    }
}