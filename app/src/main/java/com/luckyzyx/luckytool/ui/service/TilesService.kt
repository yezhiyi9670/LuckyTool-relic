package com.luckyzyx.luckytool.ui.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.telephony.SubscriptionManager
import com.drake.net.utils.scope
import com.drake.net.utils.withIO
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.IFiveGController
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.utils.SettingsPrefs
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.checkPackName
import com.luckyzyx.luckytool.utils.closeCollapse
import com.luckyzyx.luckytool.utils.getRefreshRateStatus
import com.luckyzyx.luckytool.utils.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.jumpHighPerformance
import com.luckyzyx.luckytool.utils.jumpRunningApp
import com.luckyzyx.luckytool.utils.putBoolean
import com.luckyzyx.luckytool.utils.showRefreshRate
import com.luckyzyx.luckytool.utils.toast
import com.topjohnwu.superuser.ipc.RootService

@Obfuscate
class ChargingTest : TileService() {
    override fun onClick() {
        closeCollapse()
        jumpBatteryInfo(applicationContext)
    }
}

@Obfuscate
class ProcessManager : TileService() {
    override fun onClick() {
        closeCollapse()
        jumpRunningApp(applicationContext)
    }
}

@Obfuscate
class GameAssistant : TileService() {
    override fun onStartListening() {
        scope {
            withIO {
                qsTile.state =
                    if (!checkPackName("com.oplus.games")) Tile.STATE_UNAVAILABLE else Tile.STATE_INACTIVE
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }

    override fun onClick() {
        closeCollapse()
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> ShellUtils.execCommand(
                "am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true
            )

            Tile.STATE_UNAVAILABLE -> toast(getString(R.string.game_assistant_tile_tips))
        }
    }
}

@Obfuscate
class HighPerformanceMode : TileService() {

    override fun onClick() {
        closeCollapse()
        jumpHighPerformance(this)
    }
}

@Obfuscate
class ShowFPS : TileService() {
    override fun onStartListening() {
        scope {
            withIO {
                ShellUtils.execCommand("service call SurfaceFlinger 1034 i32 2", true, true).apply {
                    if (result == 1) qsTile.state = Tile.STATE_UNAVAILABLE
                    else if (result == 0 && successMsg != null && successMsg.isNotBlank()) {
                        qsTile.state = if (getRefreshRateStatus()) Tile.STATE_ACTIVE
                        else Tile.STATE_INACTIVE
                    } else qsTile.state = Tile.STATE_UNAVAILABLE
                }
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                showRefreshRate(true)
                qsTile.state = Tile.STATE_ACTIVE
            }

            Tile.STATE_ACTIVE -> {
                showRefreshRate(false)
                qsTile.state = Tile.STATE_INACTIVE
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

@Obfuscate
class HighBrightness : TileService() {
    override fun onStartListening() {
        scope {
            withIO {
                ShellUtils.execCommand("cat /sys/kernel/oplus_display/hbm", true, true).apply {
                    if (result == 1) qsTile.state = Tile.STATE_UNAVAILABLE
                    else if (result == 0 && successMsg != null && successMsg.isNotBlank()) {
                        when (successMsg.substring(0, 1)) {
                            "0" -> qsTile.state = Tile.STATE_INACTIVE
                            "1" -> qsTile.state = Tile.STATE_ACTIVE
                        }
                    } else qsTile.state = Tile.STATE_UNAVAILABLE
                    if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
                        SettingsPrefs, "high_brightness_mode", false
                    )
                }
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 1", true)
                putBoolean(SettingsPrefs, "high_brightness_mode", true)
                dataChannel("com.android.systemui").put("high_brightness_mode", true)
                qsTile.state = Tile.STATE_ACTIVE
            }

            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/hbm 0", true)
                putBoolean(SettingsPrefs, "high_brightness_mode", false)
                dataChannel("com.android.systemui").put("high_brightness_mode", false)
                qsTile.state = Tile.STATE_INACTIVE
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

@Obfuscate
class GlobalDC : TileService() {
    override fun onStartListening() {
        scope {
            withIO {
                var oppoExist = true
                var oplusExist = true
                var isOppo = false
                var isOplus = false
                ShellUtils.execCommand("cat /sys/kernel/oppo_display/dimlayer_hbm", true, true)
                    .apply {
                        if (result == 1) oppoExist = false
                        else if (result == 0 && successMsg != null && successMsg.isNotBlank()
                            && successMsg.substring(0, 1) == "1"
                        ) isOppo = true
                    }
                ShellUtils.execCommand("cat /sys/kernel/oplus_display/dimlayer_hbm", true, true)
                    .apply {
                        if (result == 1) oplusExist = false
                        else if (result == 0 && successMsg != null && successMsg.isNotBlank()
                            && successMsg.substring(0, 1) == "1"
                        ) isOplus = true
                    }
                qsTile.state =
                    if (!(oppoExist || oplusExist)) Tile.STATE_UNAVAILABLE else if (isOppo || isOplus) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
                    SettingsPrefs, "global_dc_mode", false
                )
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oppo_display/dimlayer_hbm 1", true)
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 1", true)
                putBoolean(SettingsPrefs, "global_dc_mode", true)
                dataChannel("com.android.systemui").put("global_dc_mode", true)
                qsTile.state = Tile.STATE_ACTIVE
            }

            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /sys/kernel/oppo_display/dimlayer_hbm 0", true)
                ShellUtils.execCommand("echo > /sys/kernel/oplus_display/dimlayer_hbm 0", true)
                putBoolean(SettingsPrefs, "global_dc_mode", false)
                dataChannel("com.android.systemui").put("global_dc_mode", false)
                qsTile.state = Tile.STATE_INACTIVE
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

@Obfuscate
class TouchSamplingRate : TileService() {
    override fun onStartListening() {
        scope {
            withIO {
                ShellUtils.execCommand("cat /proc/touchpanel/game_switch_enable", true, true)
                    .apply {
                        if (result == 1) qsTile.state = Tile.STATE_UNAVAILABLE
                        else if (result == 0 && successMsg != null && successMsg.isNotBlank()) {
                            when (successMsg.substring(0, 1)) {
                                "0" -> qsTile.state = Tile.STATE_INACTIVE
                                "1" -> qsTile.state = Tile.STATE_ACTIVE
                                else -> qsTile.state = Tile.STATE_UNAVAILABLE
                            }
                        } else qsTile.state = Tile.STATE_UNAVAILABLE
                        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
                            SettingsPrefs, "touch_sampling_rate", false
                        )
                    }
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 1", true)
                putBoolean(SettingsPrefs, "touch_sampling_rate", true)
                dataChannel("com.android.systemui").put("touch_sampling_rate", true)
                qsTile.state = Tile.STATE_ACTIVE
            }

            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("echo > /proc/touchpanel/game_switch_enable 0", true)
                putBoolean(SettingsPrefs, "touch_sampling_rate", false)
                dataChannel("com.android.systemui").put("touch_sampling_rate", false)
                qsTile.state = Tile.STATE_INACTIVE
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}

@Obfuscate
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
            RootService.bind(Intent(this, FiveGControllerService::class.java),
                object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        iFiveGController = IFiveGController.Stub.asInterface(service)
                        controller(iFiveGController)
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {

                    }
                })
        }
    }

    private fun refreshData() = startFiveGController {
        scope {
            withIO {
                if (it == null) {
                    qsTile.state = Tile.STATE_UNAVAILABLE
                    return@withIO
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
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }
}

@Obfuscate
class VeryDarkMode : TileService() {
    override fun onStartListening() {
        scope {
            withIO {
                ShellUtils.execCommand(
                    "settings get secure reduce_bright_colors_activated",
                    true, true
                ).apply {
                    if (result == 1) qsTile.state =
                        Tile.STATE_UNAVAILABLE
                    else if (result == 0 && successMsg != null && successMsg.isNotBlank()) {
                        when (successMsg.substring(0, 1)) {
                            "0" -> qsTile.state = Tile.STATE_INACTIVE
                            "1" -> qsTile.state = Tile.STATE_ACTIVE
                            else -> qsTile.state = Tile.STATE_UNAVAILABLE
                        }
                    } else qsTile.state = Tile.STATE_UNAVAILABLE
                }
            }
        }.finally {
            qsTile.updateTile()
            close()
        }
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                ShellUtils.execCommand("settings put secure reduce_bright_colors_activated 1", true)
                qsTile.state = Tile.STATE_ACTIVE
            }

            Tile.STATE_ACTIVE -> {
                ShellUtils.execCommand("settings put secure reduce_bright_colors_activated 0", true)
                qsTile.state = Tile.STATE_INACTIVE
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        qsTile.updateTile()
    }
}