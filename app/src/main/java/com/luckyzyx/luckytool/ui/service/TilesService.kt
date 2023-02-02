package com.luckyzyx.luckytool.ui.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.luckyzyx.luckytool.utils.data.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.data.jumpRunningApp
import com.luckyzyx.luckytool.utils.tools.ShellUtils

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
                "1" -> tile.state = Tile.STATE_ACTIVE
            }
            tile.updateTile()
        }
    }

    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo 1 > /sys/kernel/oplus_display/hbm", true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo 0 > /sys/kernel/oplus_display/hbm", true)
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
                "1" -> tile.state = Tile.STATE_ACTIVE
            }
            tile.updateTile()
        }
    }

    override fun onClick() {
        val tile = qsTile
        when (tile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo 1 > /sys/kernel/oplus_display/dimlayer_hbm", true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo 0 > /sys/kernel/oplus_display/dimlayer_hbm", true)
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