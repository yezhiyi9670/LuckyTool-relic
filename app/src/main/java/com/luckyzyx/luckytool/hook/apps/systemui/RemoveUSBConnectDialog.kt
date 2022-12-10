package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveUSBConnectDialog : YukiBaseHooker() {
    override fun onHook() {
        //Source UsbService
        findClass("com.oplusos.systemui.notification.usb.UsbService").hook {
            injectMember {
                method {
                    name = "onUsbConnected"
                    paramCount = 1
                }
                replaceUnit {
                    val context = args(0).cast<Context>()
                    field {
                        name = "sNeedShowUsbDialog"
                        type = BooleanType
                    }.get().setFalse()
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