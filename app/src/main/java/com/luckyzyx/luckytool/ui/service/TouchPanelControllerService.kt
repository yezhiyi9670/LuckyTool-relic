package com.luckyzyx.luckytool.ui.service

import android.content.Intent
import com.luckyzyx.luckytool.ITouchPanelController
import com.luckyzyx.luckytool.utils.replaceSpace
import com.topjohnwu.superuser.ipc.RootService
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class TouchPanelControllerService : RootService() {
    companion object {
        private const val fileDir = "/proc/touchpanel/game_switch_enable"
        val file = File(fileDir)
    }

    override fun onBind(intent: Intent) = object : ITouchPanelController.Stub() {

        override fun checkTouchMode(): Boolean {
            return try {
                file.exists()
            } catch (_: Throwable) {
                false
            }
        }

        override fun getTouchMode(): Boolean {
            return try {
                when (BufferedReader(FileReader(file)).readLine()?.replaceSpace?.substring(0, 1)
                    ?.toIntOrNull()) {
                    0 -> false
                    1 -> true
                    else -> false
                }
            } catch (_: Throwable) {
                false
            }
        }

        override fun setTouchMode(status: Boolean) {
            try {
                if (file.exists()) file.writeText(if (status) "1" else "0")
            } catch (_: Throwable) {

            }
        }
    }
}