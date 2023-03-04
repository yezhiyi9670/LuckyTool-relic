package com.luckyzyx.luckytool.ui.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.IFiveGController
import com.topjohnwu.superuser.ipc.RootService

@Obfuscate
@SuppressLint("DiscouragedPrivateApi", "PrivateApi")
class FiveGControllerService : RootService() {

    companion object {

        private val telephonyService by lazy {
            Class.forName("android.os.ServiceManager")
                .getDeclaredMethod("getService", String::class.java)
                .invoke(null, Context.TELEPHONY_SERVICE)
        }

        private val iTelephony by lazy {
            Class.forName("com.android.internal.telephony.ITelephony\$Stub")
                .getDeclaredMethod("asInterface", IBinder::class.java)
                .invoke(null, telephonyService)
        }

        private val getAllowedNetworkTypesForReason by lazy {
            Class.forName("com.android.internal.telephony.ITelephony")
                .getDeclaredMethod(
                    "getAllowedNetworkTypesForReason",
                    Int::class.java,
                    Int::class.java
                )
        }

        private val setAllowedNetworkTypesForReason by lazy {
            Class.forName("com.android.internal.telephony.ITelephony")
                .getDeclaredMethod(
                    "setAllowedNetworkTypesForReason",
                    Int::class.java,
                    Int::class.java,
                    Long::class.java
                )
        }

        private val getPreferredNetworkType by lazy {
            Class.forName("com.android.internal.telephony.ITelephony")
                .getDeclaredMethod(
                    "getPreferredNetworkType",
                    Int::class.java
                )
        }

        private val setPreferredNetworkType by lazy {
            Class.forName("com.android.internal.telephony.ITelephony")
                .getDeclaredMethod(
                    "setPreferredNetworkType",
                    Int::class.java,
                    Int::class.java
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

        @delegate:SuppressLint("PrivateApi", "BlockedPrivateApi")
        private val modeLte by lazy {
            Class.forName("com.android.internal.telephony.RILConstants")
                .getDeclaredField("NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA")
                .getInt(null)
        }

        @delegate:SuppressLint("PrivateApi", "BlockedPrivateApi")
        private val modeNr by lazy {
            Class.forName("com.android.internal.telephony.RILConstants")
                .getDeclaredField("NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA")
                .getInt(null)
        }
    }

    override fun onBind(intent: Intent) = object : IFiveGController.Stub() {
        override fun checkCompatibility(subId: Int): Boolean {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    reasonUser
                    typeNr
                    setAllowedNetworkTypesForReason(
                        iTelephony,
                        subId,
                        reasonUser,
                        getAllowedNetworkTypesForReason(
                            iTelephony,
                            subId,
                            reasonUser
                        ) as Long
                    ) as Boolean
                } else {
                    modeLte
                    modeNr
                    // For Q and R.
                    setPreferredNetworkType(
                        iTelephony,
                        subId,
                        getPreferredNetworkType(iTelephony, subId) as Int
                    ) as Boolean
                }
            } catch (_: Exception) {
                false
            }
        }

        override fun getFiveGStatus(subId: Int): Boolean {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    (getAllowedNetworkTypesForReason(
                        iTelephony,
                        subId,
                        reasonUser
                    ) as Long) and typeNr != 0L
                } else {
                    // For Q and R.
                    (getPreferredNetworkType(iTelephony, subId) as Int) == modeNr
                }
            } catch (_: Exception) {
                false
            }
        }

        override fun setFiveGStatus(subId: Int, enabled: Boolean) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    var curTypes = getAllowedNetworkTypesForReason(
                        iTelephony,
                        subId,
                        reasonUser
                    ) as Long
                    curTypes = if (enabled) {
                        curTypes or typeNr
                    } else {
                        curTypes and typeNr.inv()
                    }
                    setAllowedNetworkTypesForReason(
                        iTelephony,
                        subId,
                        reasonUser,
                        curTypes
                    )
                } else {
                    // For Q and R.
                    if (enabled) {
                        setPreferredNetworkType(
                            iTelephony,
                            subId,
                            modeNr
                        )
                    } else {
                        setPreferredNetworkType(
                            iTelephony,
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