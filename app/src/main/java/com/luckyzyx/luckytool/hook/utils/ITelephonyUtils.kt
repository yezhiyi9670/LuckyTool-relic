package com.luckyzyx.luckytool.hook.utils

import android.os.IBinder
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClass
import com.highcapable.yukihookapi.hook.type.android.IBinderClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ITelephonyUtils(val classLoader: ClassLoader?) {

    val clazz = "com.android.internal.telephony.ITelephony".toClass(classLoader)
    val stub = "com.android.internal.telephony.ITelephony\$Stub".toClass(classLoader)
    val manager = "android.telephony.TelephonyManager".toClass(classLoader)
    val constants = "com.android.internal.telephony.RILConstants".toClass(classLoader)

    fun getInstance(iBinder: IBinder?): Any? {
        return stub.method {
            name = "asInterface"
            param(IBinderClass)
        }.get().call(iBinder)
    }

    /**
     * 允许的网络类型 用户原因
     */
    val reasonUser = manager.field {
        name = "ALLOWED_NETWORK_TYPES_REASON_USER"
        type = IntType
    }.get().int()

    /**
     * 网络类型位掩码 NR
     */
    val bitMaskNR = manager.field {
        name = "NETWORK_TYPE_BITMASK_NR"
        type = LongType
    }.get().long()

    /**
     * LTE模式
     */
    val modeLTE = constants.field {
        name = "NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA"
        type = IntType
    }.get().int()

    /**
     * NR模式
     */
    val modeNR = constants.field {
        name = "NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA"
        type = IntType
    }.get().int()

    /**
     * 获取允许的网络类型
     * @param instance Any?
     * @param subId Int
     * @param reason Int
     * @return Long?
     */
    fun getAllowedNetworkTypesForReason(instance: Any?, subId: Int, reason: Int): Long? {
        return clazz.method {
            name = "getAllowedNetworkTypesForReason"
            param(IntType, IntType)
        }.get(instance).invoke<Long>(subId, reason)
    }

    /**
     * 设置允许的网络类型
     * @param instance Any?
     * @param subId Int
     * @param reason Int
     * @param allowedNetworkTypes Long
     * @return Boolean?
     */
    fun setAllowedNetworkTypesForReason(
        instance: Any?,
        subId: Int,
        reason: Int,
        allowedNetworkTypes: Long
    ): Boolean? {
        return clazz.method {
            name = "setAllowedNetworkTypesForReason"
            param(IntType, IntType, LongType)
        }.get(instance).invoke<Boolean>(subId, reason, allowedNetworkTypes)
    }

    /**
     * 获取首选网络类型 by A11
     * @param instance Any?
     * @param subId Int
     * @return Int?
     */
    fun getPreferredNetworkType(instance: Any?, subId: Int): Int? {
        return clazz.method {
            name = "getPreferredNetworkType"
            param(IntType)
        }.get(instance).invoke<Int>(subId)
    }

    /**
     * 设置首选网络类型 by A11
     * @param instance Any?
     * @param subId Int
     * @param networkType Int
     * @return Boolean?
     */
    fun setPreferredNetworkType(instance: Any?, subId: Int, networkType: Int): Boolean? {
        return clazz.method {
            name = "setPreferredNetworkType"
            param(IntType, IntType)
        }.get(instance).invoke<Boolean>(subId, networkType)
    }
}