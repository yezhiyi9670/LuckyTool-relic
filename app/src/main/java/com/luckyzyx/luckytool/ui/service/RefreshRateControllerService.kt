package com.luckyzyx.luckytool.ui.service

import android.content.Intent
import android.os.Parcel
import android.os.RemoteException
import com.luckyzyx.luckytool.IRefreshRateController
import com.luckyzyx.luckytool.hook.utils.ServiceManagerUtils
import com.topjohnwu.superuser.ipc.RootService

class RefreshRateControllerService : RootService() {
    companion object {
        private const val serviceName = "SurfaceFlinger"
        private const val interfaceClazz = "android.ui.ISurfaceComposer"

        private val surfaceFlinger by lazy {
            ServiceManagerUtils(null).getService(serviceName)
        }
    }

    override fun onBind(intent: Intent) = object : IRefreshRateController.Stub() {
        override fun getRefreshRateDisplay(): Boolean {
            try {
                if (surfaceFlinger != null) {
                    val obtain = Parcel.obtain()
                    val obtain2 = Parcel.obtain()
                    obtain.writeInterfaceToken(interfaceClazz)
                    obtain.writeInt(2)
                    surfaceFlinger?.transact(1034, obtain, obtain2, 0)
                    val status = obtain2.readBoolean()
                    obtain2.recycle()
                    obtain.recycle()
                    return status
                }
                return false
            } catch (e: RemoteException) {
                return false
            }
        }

        override fun setRefreshRateDisplay(status: Boolean) {
            try {
                if (surfaceFlinger != null) {
                    val obtain = Parcel.obtain()
                    obtain.writeInterfaceToken(interfaceClazz)
                    obtain.writeInt(if (status) 1 else 0)
                    surfaceFlinger?.transact(1034, obtain, null, 0)
                    obtain.recycle()
                }
            } catch (_: RemoteException) {
            }
        }
    }
}