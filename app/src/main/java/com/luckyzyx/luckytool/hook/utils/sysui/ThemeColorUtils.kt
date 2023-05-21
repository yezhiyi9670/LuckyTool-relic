package com.luckyzyx.luckytool.hook.utils.sysui

import android.graphics.Color
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.hooker.HookDialogRelated.toClass

@Suppress("unused")
class ThemeColorUtils(val classLoader: ClassLoader?) {

    val clazz = "com.oplusos.systemui.util.ThemeColorUtils".toClass(classLoader)

    val controlCenterRedOne = Color.parseColor("#c41442")

    fun getColor(int: Int): Int? {
        return clazz.method {
            name = "getColor"
            paramCount = 1
        }.get().invoke<Int>(int)
    }
}