package com.luckyzyx.luckytool.hook.scope.camera

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

object RemoveWatermarkWordLimit : YukiBaseHooker() {
    override fun onHook() {
        // Source CameraSubSettingFragment
        // Log camera_namelength_outofrange
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val clazz = when (appSet[2]) {
            "8d5b992", "38e5b1a" -> "com.oplus.camera.ui.menu.setting.p\$7"
            "c7732c4" -> "com.oplus.camera.ui.menu.setting.p\$5"
            else -> "com.oplus.camera.setting.j\$5"
        }
        findClass(clazz).hook {
            injectMember {
                method {
                    name = "filter"
                    returnType = CharSequenceClass
                }
                intercept()
            }
        }
    }
}