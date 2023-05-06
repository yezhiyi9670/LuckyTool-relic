package com.luckyzyx.luckytool.ui.service

import android.content.Context
import android.content.Intent
import com.luckyzyx.luckytool.IFiveGController
import com.luckyzyx.luckytool.hook.utils.ITelephonyUtils
import com.luckyzyx.luckytool.hook.utils.ServiceManagerUtils
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.SDK
import com.topjohnwu.superuser.ipc.RootService

class FiveGControllerService : RootService() {

    companion object {

        private val telephonyService by lazy {
            ServiceManagerUtils(null).getService(Context.TELEPHONY_SERVICE)
        }

        private val iTelephony by lazy {
            ITelephonyUtils(null).getInstance(telephonyService)
        }
    }

    override fun onBind(intent: Intent) = object : IFiveGController.Stub() {
        override fun checkCompatibility(subId: Int): Boolean {
            return try {
                if (SDK >= A12) {
                    ITelephonyUtils(null).let {
                        it.setAllowedNetworkTypesForReason(
                            iTelephony, subId, it.reasonUser,
                            it.getAllowedNetworkTypesForReason(iTelephony, subId, it.reasonUser)!!
                        )!!
                    }
                } else {
                    // For Q and R.
                    ITelephonyUtils(null).let {
                        it.setPreferredNetworkType(
                            iTelephony, subId,
                            it.getPreferredNetworkType(iTelephony, subId)!!
                        )!!
                    }
                }
            } catch (_: Exception) {
                false
            }
        }

        override fun getFiveGStatus(subId: Int): Boolean {
            return try {
                if (SDK >= A12) {
                    ITelephonyUtils(null).let {
                        it.getAllowedNetworkTypesForReason(
                            iTelephony, subId, it.reasonUser
                        )!! and it.bitMaskNR != 0L
                    }
                } else {
                    // For Q and R.
                    ITelephonyUtils(null).let {
                        it.getPreferredNetworkType(iTelephony, subId)!! == it.modeNR
                    }
                }
            } catch (_: Exception) {
                false
            }
        }

        override fun setFiveGStatus(subId: Int, enabled: Boolean) {
            try {
                if (SDK >= A12) {
                    ITelephonyUtils(null).let {
                        var curTypes = it.getAllowedNetworkTypesForReason(
                            iTelephony, subId, it.reasonUser
                        )!!
                        curTypes = if (enabled) curTypes or it.bitMaskNR
                        else curTypes and it.bitMaskNR.inv()
                        it.setAllowedNetworkTypesForReason(
                            iTelephony, subId, it.reasonUser, curTypes
                        )
                    }
                } else {
                    // For Q and R.
                    ITelephonyUtils(null).let {
                        it.setPreferredNetworkType(
                            iTelephony, subId, if (enabled) it.modeNR else it.modeLTE
                        )
                    }
                }
            } catch (_: Exception) {

            }
        }
    }
}