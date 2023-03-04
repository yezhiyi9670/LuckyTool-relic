package com.luckyzyx.luckytool.ui.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.android.internal.telephony.ITelephony
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.IFiveGController
import com.luckyzyx.luckytool.utils.data.A12
import com.luckyzyx.luckytool.utils.data.SDK
import com.topjohnwu.superuser.ipc.RootService

@Obfuscate
@SuppressLint("DiscouragedPrivateApi", "BlockedPrivateApi", "PrivateApi")
class FiveGControllerService : RootService() {

    companion object {
        private val iTelephony by lazy {
            ITelephony.Stub.asInterface(
                Class.forName("android.os.ServiceManager")
                    .getDeclaredMethod("getService", String::class.java)
                    .invoke(null, Context.TELEPHONY_SERVICE) as IBinder?
            )
        }
        private val reasonUser by lazy {
            Class.forName("android.telephony.TelephonyManager")
                .getDeclaredField("ALLOWED_NETWORK_TYPES_REASON_USER")
                .getInt(null)
        }
        private val typeNr by lazy {
            Class.forName("android.telephony.TelephonyManager")
                .getDeclaredField("NETWORK_TYPE_BITMASK_NR")
                .getLong(null)
        }

        private val modeLte by lazy {
            Class.forName("com.android.internal.telephony.RILConstants")
                .getDeclaredField("NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA")
                .getInt(null)
        }

        private val modeNr by lazy {
            Class.forName("com.android.internal.telephony.RILConstants")
                .getDeclaredField("NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA")
                .getInt(null)
        }
    }

    override fun onBind(intent: Intent) = object : IFiveGController.Stub() {
        override fun compatibilityCheck(subId: Int): Boolean {
            return try {
                if (SDK >= A12) {
                    reasonUser
                    typeNr
                    iTelephony.setAllowedNetworkTypesForReason(
                        subId,
                        reasonUser,
                        iTelephony.getAllowedNetworkTypesForReason(
                            subId,
                            reasonUser
                        )
                    )
                } else {
                    modeLte
                    modeNr
                    // For Q and R.
                    iTelephony.setPreferredNetworkType(
                        subId,
                        iTelephony.getPreferredNetworkType(subId)
                    )
                }
            } catch (_: Exception) {
                false
            }
        }

        override fun getFivegEnabled(subId: Int): Boolean {
            return try {
                if (SDK >= A12) {
                    iTelephony.getAllowedNetworkTypesForReason(
                        subId,
                        reasonUser
                    ) and typeNr != 0L
                } else {
                    // For Q and R.
                    iTelephony.getPreferredNetworkType(subId) ==
                            modeNr
                }
            } catch (_: Exception) {
                false
            }
        }

        override fun setFivegEnabled(subId: Int, enabled: Boolean) {
            try {
                if (SDK >= A12) {
                    var curTypes = iTelephony.getAllowedNetworkTypesForReason(
                        subId,
                        reasonUser
                    )
                    curTypes = if (enabled) {
                        curTypes or typeNr
                    } else {
                        curTypes and typeNr.inv()
                    }
                    iTelephony.setAllowedNetworkTypesForReason(
                        subId,
                        reasonUser,
                        curTypes
                    )
                } else {
                    // For Q and R.
                    if (enabled) {
                        iTelephony.setPreferredNetworkType(
                            subId,
                            modeNr
                        )
                    } else {
                        iTelephony.setPreferredNetworkType(
                            subId,
                            modeLte
                        )
                    }
                }
            } catch (_: Exception) {

            }
        }
    }
}