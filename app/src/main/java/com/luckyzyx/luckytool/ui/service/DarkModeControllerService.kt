package com.luckyzyx.luckytool.ui.service

import android.content.Intent
import com.luckyzyx.luckytool.IDarkModeController
import com.luckyzyx.luckytool.hook.utils.IColorDisplayUtils
import com.topjohnwu.superuser.ipc.RootService

class DarkModeControllerService : RootService() {
    companion object {

        private val iColorDisplayManager by lazy {
            IColorDisplayUtils(null).getInstance()
        }
    }

    override fun onBind(intent: Intent) = object : IDarkModeController.Stub() {

        override fun checkDarkMode(): Boolean {
            return iColorDisplayManager != null
        }

        override fun getDarkMode(): Boolean {
            return try {
                IColorDisplayUtils(null).isReduceBrightColorsActivated(iColorDisplayManager) == true
            } catch (_: Throwable) {
                false
            }
        }

        override fun setDarkMode(status: Boolean) {
            try {
                IColorDisplayUtils(null)
                    .setReduceBrightColorsActivated(iColorDisplayManager, status)
            } catch (_: Throwable) {

            }
        }
    }
}