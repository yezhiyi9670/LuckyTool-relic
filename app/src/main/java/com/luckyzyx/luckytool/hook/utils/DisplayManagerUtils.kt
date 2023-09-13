package com.luckyzyx.luckytool.hook.utils

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.IBinder
import android.view.Display
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.extends
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.android.IBinderClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.luckytool.hook.scope.systemui.FingerPrintIconAnim.toClass


@Suppress("unused", "MemberVisibilityCanBePrivate")
class DisplayManagerUtils(val classLoader: ClassLoader?) {

    val clazz = "android.hardware.display.DisplayManager".toClass(classLoader)
    val displayClazz = "android.view.Display".toClass(classLoader)
    val displayInfoClazz = "android.view.DisplayInfo".toClass(classLoader)
    val addressPhysicalClazz = "android.view.DisplayAddress\$Physical".toClass(classLoader)
    val surfaceControlClazz = "android.view.SurfaceControl".toClass(classLoader)

    fun getService(context: Context): DisplayManager {
        return context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    fun Display.getDisplayInfo(outDisplayInfo: Any?): Boolean? {
        return displayClazz.method {
            name = "getDisplayInfo"
            param(displayInfoClazz)
        }.get(this).invoke<Boolean>(outDisplayInfo)
    }

    fun getDefaultDisplayToken(displayInfo: Any): IBinder? {
        val address = displayInfo.current().field { name = "address" }.any() ?: return null
        val extend = (address.javaClass.extends(addressPhysicalClazz))
        return if (extend) {
            val physicalDisplayId = getPhysicalDisplayId(address)
            getPhysicalDisplayToken(physicalDisplayId)
        } else getInternalDisplayToken()
    }

    fun getPhysicalDisplayId(physical: Any?): Long? {
        return addressPhysicalClazz.method {
            name = "getPhysicalDisplayId"
            emptyParam()
        }.get(physical).invoke<Long>()
    }

    fun getPhysicalDisplayToken(physicalDisplayId: Long?): IBinder? {
        return surfaceControlClazz.method {
            name = "getPhysicalDisplayToken"
            param(LongType)
        }.get().invoke<IBinder>(physicalDisplayId)
    }

    fun getInternalDisplayToken(): IBinder? {
        return surfaceControlClazz.method {
            name = "getInternalDisplayToken"
            emptyParam()
        }.get().invoke<IBinder>()
    }

    fun getDynamicDisplayInfo(displayToken: IBinder?): Any? {
        return surfaceControlClazz.method {
            name = "getDynamicDisplayInfo"
            param(IBinderClass)
        }.get().call(displayToken)
    }
}