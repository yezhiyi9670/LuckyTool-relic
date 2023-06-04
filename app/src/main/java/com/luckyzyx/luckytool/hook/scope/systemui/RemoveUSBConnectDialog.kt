package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

object RemoveUSBConnectDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source UsbService
        VariousClass(
            "com.coloros.systemui.notification.usb.UsbService", //A11
            "com.oplusos.systemui.notification.usb.UsbService"
        ).hook {
            injectMember {
                method {
                    name = "onUsbConnected"
                    paramCount = 1
                }
                replaceUnit {
                    val context = args(0).cast<Context>() ?: return@replaceUnit
                    field {
                        name = "sNeedShowUsbDialog"
                        type = BooleanType
                    }.get().setFalse()
                    method {
                        name = "onUsbSelect"
                        paramCount = 1
                    }.get(instance).call(1)
                    method {
                        name = "updateAdbNotification"
                        paramCount = 1
                    }.get(instance).call(context)
                    method {
                        name = "updateUsbNotification"
                        paramCount = 2
                    }.get(instance).call(context, 1)
                }
            }
        }
    }
}