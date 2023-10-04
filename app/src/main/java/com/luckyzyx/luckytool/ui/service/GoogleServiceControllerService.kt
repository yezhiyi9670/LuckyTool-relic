package com.luckyzyx.luckytool.ui.service

import android.content.Intent
import com.luckyzyx.luckytool.IGoogleServiceController
import com.luckyzyx.luckytool.utils.LogUtils
import com.luckyzyx.luckytool.utils.ShellUtils
import com.topjohnwu.superuser.ipc.RootService

class GoogleServiceControllerService : RootService() {
    val tag = "GoogleServiceControllerService"

    companion object {
        //Source Settings OplusGoogleSettingsFragment
        private const val key = "customize_control_cn_gms"
    }

    override fun onBind(intent: Intent) = object : IGoogleServiceController.Stub() {
        override fun getGoogleStatus(): Boolean {
            return try {
                val result = ShellUtils.execCommand(
                    "settings get system $key",
                    true, true
                )
                LogUtils.d(
                    tag, "getGoogleStatus",
                    "result -> ${result.result} | ${result.successMsg} | ${result.errorMsg}"
                )
                result.successMsg.toIntOrNull() == 1
            } catch (e: Exception) {
                LogUtils.d(tag, "getGoogleStatus", "$e")
                false
            }
        }

        override fun setGoogleStatus(status: Boolean) {
            try {
                val result = ShellUtils.execCommand(
                    "settings put system $key ${if (status) 1 else 0}",
                    true, true
                )
                LogUtils.d(tag, "setGoogleStatus", "$status -> ${result.result == 0}")
            } catch (e: Exception) {
                LogUtils.d(tag, "setGoogleStatus", "$e")
            }
        }
    }
}