package com.luckyzyx.luckytool.hook.scope.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveControlCenterMydevice : YukiBaseHooker() {
    override fun onHook() {
        // Source MyDevicePanel
        findClass("com.oplus.systemui.qs.mydevice.MyDevicePanel").hook {
            injectMember {
                method { name = "onFinishInflate" }
                afterHook {
                    (field { name = "mDeviceChildContainer" }.get(instance)
                        .cast<View>())?.isVisible = false
                    instance<View>().setOnClickListener(null)
                    instance<View>().setOnLongClickListener(null)
                }
            }
        }
    }
}