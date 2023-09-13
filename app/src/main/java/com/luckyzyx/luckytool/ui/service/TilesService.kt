package com.luckyzyx.luckytool.ui.service

import android.content.ComponentName
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
import com.luckyzyx.luckytool.utils.bindRootService
import com.luckyzyx.luckytool.utils.closeCollapse
import com.luckyzyx.luckytool.utils.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.jumpHighPerformance
import com.luckyzyx.luckytool.utils.jumpRunningApp
import com.luckyzyx.luckytool.utils.putBoolean

class ChargingTest : TileService() {
    override fun onClick() {
        closeCollapse()
        jumpBatteryInfo(this)
    }
}

class ProcessManager : TileService() {
    override fun onClick() {
        closeCollapse()
        jumpRunningApp(this)
    }
}

class HighPerformanceMode : TileService() {

    override fun onClick() {
        closeCollapse()
        jumpHighPerformance(this)
    }
}

class ShowFPS : TileService() {
    private var controller: IRefreshRateController? = null

    override fun onStartListening() = startController()

    override fun onClick() {
        if (qsTile.state == Tile.STATE_INACTIVE) controller?.refreshRateDisplay = true
        else if (qsTile.state == Tile.STATE_ACTIVE) controller?.refreshRateDisplay = false
        refreshData()
    }

    private fun startController() {
        if (controller == null) bindRootService(RefreshRateControllerService::class.java,
            { _: ComponentName?, iBinder: IBinder? ->
                controller = IRefreshRateController.Stub.asInterface(iBinder)
                refreshData()
            }, {}, true
        )
    }

    private fun refreshData() {
        qsTile.state = if (controller == null) Tile.STATE_UNAVAILABLE
        else if (controller!!.refreshRateDisplay) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}

class HighBrightness : TileService() {
    private var controller: IHighBrightnessController? = null
    override fun onStartListening() = startController()

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                controller?.highBrightnessMode = true
                putBoolean(SettingsPrefs, "high_brightness_mode", true)
                dataChannel("com.android.systemui").put("high_brightness_mode", true)
            }

            Tile.STATE_ACTIVE -> {
                controller?.highBrightnessMode = false
                putBoolean(SettingsPrefs, "high_brightness_mode", false)
                dataChannel("com.android.systemui").put("high_brightness_mode", false)
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController() {
        if (controller == null) bindRootService(HighBrightnessControllerService::class.java,
            { _: ComponentName?, iBinder: IBinder? ->
                controller = IHighBrightnessController.Stub.asInterface(iBinder)
                refreshData()
            }, {}, true
        )
    }

    private fun refreshData() {
        qsTile.state = if (controller == null) Tile.STATE_UNAVAILABLE
        else if (!controller!!.checkHighBrightnessMode()) Tile.STATE_UNAVAILABLE
        else if (controller!!.highBrightnessMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
            SettingsPrefs, "high_brightness_mode", false
        )
    }
}

class GlobalDC : TileService() {
    private var controller: IGlobalDCController? = null
    override fun onStartListening() = startController()

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                controller?.globalDCMode = true
                putBoolean(SettingsPrefs, "global_dc_mode", true)
                dataChannel("com.android.systemui").put("global_dc_mode", true)
            }

            Tile.STATE_ACTIVE -> {
                controller?.globalDCMode = false
                putBoolean(SettingsPrefs, "global_dc_mode", false)
                dataChannel("com.android.systemui").put("global_dc_mode", false)
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController() {
        if (controller == null) bindRootService(GlobalDCControllerService::class.java,
            { _: ComponentName?, iBinder: IBinder? ->
                controller = IGlobalDCController.Stub.asInterface(iBinder)
                refreshData()
            }, {}, true
        )
    }

    private fun refreshData() {
        qsTile.state = if (controller == null) Tile.STATE_UNAVAILABLE
        else if (!controller!!.checkGlobalDCMode()) Tile.STATE_UNAVAILABLE
        else if (controller!!.globalDCMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
            SettingsPrefs, "global_dc_mode", false
        )
    }
}

class TouchSamplingRate : TileService() {
    private var controller: ITouchPanelController? = null
    override fun onStartListening() = startController()

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> {
                controller?.touchMode = true
                putBoolean(SettingsPrefs, "touch_sampling_rate", true)
                dataChannel("com.android.systemui").put("touch_sampling_rate", true)
            }

            Tile.STATE_ACTIVE -> {
                controller?.touchMode = false
                putBoolean(SettingsPrefs, "touch_sampling_rate", false)
                dataChannel("com.android.systemui").put("touch_sampling_rate", false)
            }

            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController() {
        if (controller == null) bindRootService(TouchPanelControllerService::class.java,
            { _: ComponentName?, iBinder: IBinder? ->
                controller = ITouchPanelController.Stub.asInterface(iBinder)
                refreshData()
            }, {}, true
        )
    }

    private fun refreshData() {
        qsTile.state = if (controller == null) Tile.STATE_UNAVAILABLE
        else if (!controller!!.checkTouchMode()) Tile.STATE_UNAVAILABLE
        else if (controller!!.touchMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
        if (qsTile.state == Tile.STATE_UNAVAILABLE) putBoolean(
            SettingsPrefs, "touch_sampling_rate", false
        )
    }
}

class FiveG : TileService() {
    private var controller: IFiveGController? = null
    override fun onStartListening() = startController()

    override fun onClick() {
        val subId = SubscriptionManager.getDefaultDataSubscriptionId()
        if (qsTile.state == Tile.STATE_INACTIVE) controller?.setFiveGStatus(subId, true)
        else if (qsTile.state == Tile.STATE_ACTIVE) controller?.setFiveGStatus(subId, false)
        refreshData()
    }

    private fun startController() {
        if (controller == null) bindRootService(FiveGControllerService::class.java,
            { _: ComponentName?, iBinder: IBinder? ->
                controller = IFiveGController.Stub.asInterface(iBinder)
                refreshData()
            }, {}, true
        )
    }

    private fun refreshData() {
        val subId = SubscriptionManager.getDefaultDataSubscriptionId()
        qsTile.state = if (controller == null) Tile.STATE_UNAVAILABLE
        else if (!controller!!.checkCompatibility(subId)) Tile.STATE_UNAVAILABLE
        else if (controller!!.getFiveGStatus(subId)) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}

class VeryDarkMode : TileService() {
    private var controller: IDarkModeController? = null

    override fun onStartListening() = startController()

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_INACTIVE -> controller?.darkMode = true
            Tile.STATE_ACTIVE -> controller?.darkMode = false
            Tile.STATE_UNAVAILABLE -> {}
        }
        refreshData()
    }

    private fun startController() {
        if (controller == null) bindRootService(DarkModeControllerService::class.java,
            { _: ComponentName?, iBinder: IBinder? ->
                controller = IDarkModeController.Stub.asInterface(iBinder)
                refreshData()
            }, {}, true
        )
    }

    private fun refreshData() {
        qsTile.state = if (controller == null) Tile.STATE_UNAVAILABLE
        else if (!controller!!.checkDarkMode()) Tile.STATE_UNAVAILABLE
        else if (controller!!.darkMode) Tile.STATE_ACTIVE
        else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }
}