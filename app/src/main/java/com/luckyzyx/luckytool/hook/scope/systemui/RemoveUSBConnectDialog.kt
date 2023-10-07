package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.factory.method

object RemoveUSBConnectDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source UsbService
        VariousClass(
            "com.coloros.systemui.notification.usb.UsbService", //A11
            "com.oplusos.systemui.notification.usb.UsbService",
            "com.oplus.systemui.usb.UsbService" //C14
        ).toClass().apply {
            method { name = "onUsbConnected" }.hook {
                replaceUnit {
                    val context = args().first().cast<Context>() ?: return@replaceUnit
                    method { name = "onUsbSelect" }.get(instance).call(1)
                    method { name = "updateAdbNotification" }.get(instance).call(context)
                    method { name = "updateUsbNotification" }.get(instance).call(context, 1)
                    method { name = "changeUsbConfig" }.get(instance).call(context, 1)
                }
            }
            method { name = "updateUsbNotification" }.hook {
                before { field { name = "sNeedShowUsbDialog" }.get().setFalse() }
            }
        }
    }
}