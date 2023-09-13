package com.luckyzyx.luckytool.ui.service

import android.content.Intent
import android.os.Parcel
import android.os.RemoteException
import com.highcapable.yukihookapi.hook.factory.buildOf
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.IRefreshRateController
import com.luckyzyx.luckytool.hook.utils.DisplayManagerUtils
import com.luckyzyx.luckytool.hook.utils.ServiceManagerUtils
import com.luckyzyx.luckytool.utils.DisplayMode
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

        override fun getSupportModes(): ArrayList<DisplayMode> {
            val list = ArrayList<DisplayMode>()
            val context = this@RefreshRateControllerService
            return try {
                DisplayManagerUtils(null).apply {
                    val displayManager = getService(context)
                    val display = displayManager.getDisplay(0)
                    val displayInfo = displayInfoClazz.buildOf { emptyParam() } ?: return list
                    if (display.getDisplayInfo(displayInfo) != true) return list
                    val mDefaultDisplayToken = getDefaultDisplayToken(displayInfo)
                    val dynamicInfo = getDynamicDisplayInfo(mDefaultDisplayToken)
                    val supportedDisplayModes = dynamicInfo?.current()?.field {
                        name = "supportedDisplayModes"
                    }?.array<Any>()

                    supportedDisplayModes?.forEach {
                        val id = it.current().field { name = "id" }.cast<Int>() ?: return@forEach
                        val width = it.current().field { name = "width" }.cast<Int>()
                        val height = it.current().field { name = "height" }.cast<Int>()
                        val xDpi = it.current().field { name = "xDpi" }.cast<Float>()
                        val yDpi = it.current().field { name = "yDpi" }.cast<Float>()
                        val refreshRate = it.current().field { name = "refreshRate" }.cast<Float>()
                        val appVsyncOffsetNanos = it.current().field {
                            name = "appVsyncOffsetNanos"
                        }.cast<Long>()
                        val presentationDeadlineNanos = it.current().field {
                            name = "presentationDeadlineNanos"
                        }.cast<Long>()
                        val group = it.current().field { name = "group" }.cast<Int>()
                        list.add(
                            id, DisplayMode(
                                id, width, height, xDpi, yDpi, refreshRate,
                                appVsyncOffsetNanos, presentationDeadlineNanos, group
                            )
                        )
                    }
                }
                list
            } catch (_: Throwable) {
                list
            }
        }

        override fun setRefreshRateMode(modeId: Int) {
            try {
                if (surfaceFlinger != null) {
                    val obtain = Parcel.obtain()
                    obtain.writeInterfaceToken(interfaceClazz)
                    obtain.writeInt(modeId)
                    surfaceFlinger?.transact(1035, obtain, null, 0)
                    obtain.recycle()
                }
            } catch (_: Throwable) {

            }
        }

        override fun resetRefreshRateMode() {
            try {
                setRefreshRateMode(-1)
            } catch (_: Throwable) {

            }
        }
    }
}