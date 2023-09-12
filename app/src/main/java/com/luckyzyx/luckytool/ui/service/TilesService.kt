package com.luckyzyx.luckytool.ui.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.telephony.SubscriptionManager
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.luckyzyx.luckytool.IDarkModeController
import com.luckyzyx.luckytool.IFiveGController
import com.luckyzyx.luckytool.IGlobalDCController
import com.luckyzyx.luckytool.IHighBrightnessController
import com.luckyzyx.luckytool.IRefreshRateController
import com.luckyzyx.luckytool.ITouchPanelController
import com.luckyzyx.luckytool.utils.SettingsPrefs
import com.luckyzyx.luckytool.utils.closeCollapse
import com.luckyzyx.luckytool.utils.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.jumpHighPerformance
import com.luckyzyx.luckytool.utils.jumpRunningApp
import com.luckyzyx.luckytool.utils.putBoolean
import com.topjohnwu.superuser.ipc.RootService

class ChargingTest : TileService() {
    override fun onClick() {
        closeCollapse()
        jumpBatteryInfo(applicationContext)
    }
}

class ProcessManager : TileService() {
    override fun onClick() {
        closeCollapse()
        jumpRunningApp(applicationContext)
    }
}

class HighPerformanceMode : TileService() {

    override fun onClick() {
        closeCollapse()
        jumpHighPerformance(this)
    }
}

class ShowFPS : TileService() {
    private var iRefreshRateController: IRefreshRateController? = null

    override fun onStartListening() = refreshData()

    override fun onClick() = startController {
        if (qsTile.state == Tile.STATE_INACTIVE) it?.refreshRateDisplay = true
        else if (qsTile.state == Tile.STATE_ACTIVE) it?.refreshRateDisplay = false
        refreshData()
    }

    private fun startController(controller: (IRefreshRateController?) -> Unit) {
        if (iRefreshRateController != null) controller(iRefreshRateController)
        else RootService.bind(Intent(this, RefreshRateControllerService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(
                    name: ComponentName?,
                    service: IBinder?
                ) {
                    iRefreshRateController = IRefreshRateController.Stub.asInterface(service)
                    controller(iRefreshRateController)
                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            })
    }

    private fun refreshData() = startController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            return@startController
        }
        qsTile.state = if (it.refreshRateDisplay) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}

class HighBrightness : TileService() {
    private var iHighBrightnessControllerService: IHighBrightnessController? = null
    override fun onStartListening() = refreshData()

    override fun onClick() = startController {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                it?.highBrightnessMode = true
                putBoolean(SettingsPrefs, "high_brightness_mode", true)
                dataChannel("com.android.systemui").put("high_brightness_mode", true)
            }

            Tile.STATE_ACTIVE -> {
                it?.highBrightnessMode = false
                putBoolean(SettingsPrefs, "high_brightness_mode", false)
                dataChannel("com.android.systemui").put("high_brightness_mode", false)
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController(controller: (IHighBrightnessController?) -> Unit) {
        if (iHighBrightnessControllerService != null) controller(iHighBrightnessControllerService)
        else RootService.bind(Intent(this, HighBrightnessControllerService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    iHighBrightnessControllerService =
                        IHighBrightnessController.Stub.asInterface(service)
                    controller(iHighBrightnessControllerService)
                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            })
    }

    private fun refreshData() = startController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            return@startController
        }
        qsTile.state = if (!it.checkHighBrightnessMode()) Tile.STATE_UNAVAILABLE
        else if (it.highBrightnessMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
            SettingsPrefs, "high_brightness_mode", false
        )
    }
}

class GlobalDC : TileService() {
    private var iGlobalDCController: IGlobalDCController? = null
    override fun onStartListening() = refreshData()

    override fun onClick() = startController {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                it?.globalDCMode = true
                putBoolean(SettingsPrefs, "global_dc_mode", true)
                dataChannel("com.android.systemui").put("global_dc_mode", true)
            }

            Tile.STATE_ACTIVE -> {
                it?.globalDCMode = false
                putBoolean(SettingsPrefs, "global_dc_mode", false)
                dataChannel("com.android.systemui").put("global_dc_mode", false)
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController(controller: (IGlobalDCController?) -> Unit) {
        if (iGlobalDCController != null) controller(iGlobalDCController)
        else RootService.bind(Intent(this, GlobalDCControllerService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    iGlobalDCController = IGlobalDCController.Stub.asInterface(service)
                    controller(iGlobalDCController)
                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            })
    }

    private fun refreshData() = startController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            return@startController
        }
        qsTile.state = if (!it.checkGlobalDCMode()) Tile.STATE_UNAVAILABLE
        else if (it.globalDCMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
            SettingsPrefs, "global_dc_mode", false
        )
    }
}

class TouchSamplingRate : TileService() {
    private var iTouchPanelController: ITouchPanelController? = null
    override fun onStartListening() = refreshData()

    override fun onClick() = startController {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                it?.touchMode = true
                putBoolean(SettingsPrefs, "touch_sampling_rate", true)
                dataChannel("com.android.systemui").put("touch_sampling_rate", true)
            }

            Tile.STATE_ACTIVE -> {
                it?.touchMode = false
                putBoolean(SettingsPrefs, "touch_sampling_rate", false)
                dataChannel("com.android.systemui").put("touch_sampling_rate", false)
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController(controller: (ITouchPanelController?) -> Unit) {
        if (iTouchPanelController != null) controller(iTouchPanelController)
        else RootService.bind(Intent(this, TouchPanelControllerService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    iTouchPanelController = ITouchPanelController.Stub.asInterface(service)
                    controller(iTouchPanelController)
                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            })
    }

    private fun refreshData() = startController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            return@startController
        }
        qsTile.state = if (!it.checkTouchMode()) Tile.STATE_UNAVAILABLE
        else if (it.touchMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
            SettingsPrefs, "touch_sampling_rate", false
        )
    }
}

class FiveG : TileService() {
    private var iFiveGController: IFiveGController? = null
    override fun onStartListening() = refreshData()

    override fun onClick() = startController {
        val subId = SubscriptionManager.getDefaultDataSubscriptionId()
        if (qsTile.state == Tile.STATE_INACTIVE) it?.setFiveGStatus(subId, true)
        else if (qsTile.state == Tile.STATE_ACTIVE) it?.setFiveGStatus(subId, false)
        refreshData()
    }

    private fun startController(controller: (IFiveGController?) -> Unit) {
        if (iFiveGController != null) controller(iFiveGController)
        else RootService.bind(Intent(this, FiveGControllerService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    iFiveGController = IFiveGController.Stub.asInterface(service)
                    controller(iFiveGController)
                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            })
    }

    private fun refreshData() = startController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            return@startController
        }
        val subId = SubscriptionManager.getDefaultDataSubscriptionId()
        qsTile.state = if (!it.checkCompatibility(subId)) Tile.STATE_UNAVAILABLE
        else if (it.getFiveGStatus(subId)) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}

class VeryDarkMode : TileService() {
    private var iDarkModeController: IDarkModeController? = null

    override fun onStartListening() = refreshData()

    override fun onClick() = startController {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> it?.darkMode = true
            Tile.STATE_ACTIVE -> it?.darkMode = false
            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController(controller: (IDarkModeController?) -> Unit) {
        if (iDarkModeController != null) controller(iDarkModeController)
        else RootService.bind(Intent(this, DarkModeControllerService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    iDarkModeController = IDarkModeController.Stub.asInterface(service)
                    controller(iDarkModeController)
                }

                override fun onServiceDisconnected(name: ComponentName?) {

                }
            })
    }

    private fun refreshData() = startController {
        if (it == null) {
            qsTile.state = Tile.STATE_UNAVAILABLE
            return@startController
        }
        qsTile.state = if (!it.checkDarkMode()) Tile.STATE_UNAVAILABLE
        else if (it.darkMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}