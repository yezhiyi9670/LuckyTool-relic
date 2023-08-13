package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveUSBConnectDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source UsbService
        VariousClass(
            "com.coloros.systemui.notification.usb.UsbService", //A11
            "com.oplusos.systemui.notification.usb.UsbService"
        ).hook {
            injectMember {
                method { name = "onUsbConnected" }
                replaceUnit {
                    val context = args(0).cast<Context>() ?: return@replaceUnit
                    method { name = "onUsbSelect" }.get(instance).call(1)
                    method { name = "updateAdbNotification" }.get(instance).call(context)
                    method { name = "updateUsbNotification" }.get(instance).call(context, 1)
                    method { name = "changeUsbConfig" }.get(instance).call(context, 1)
                }
            }
            injectMember {
                method { name = "updateUsbNotification" }
                beforeHook { field { name = "sNeedShowUsbDialog" }.get().setFalse() }
            }
        }
    }
}