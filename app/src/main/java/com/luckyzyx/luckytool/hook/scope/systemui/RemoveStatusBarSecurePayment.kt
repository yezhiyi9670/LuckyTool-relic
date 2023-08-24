package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveStatusBarSecurePayment : YukiBaseHooker() {
    override fun onHook() {
        //安全支付图标
        VariousClass(
            "com.oplusos.systemui.ext.SecurePaymentControllerExt",
            "com.oplus.systemui.statusbar.phone.securepay.SecurePaymentControllerExImpl",
            "com.oplus.systemui.statusbar.phone.dynamic.SecurePaymentController" //C14
        ).hook {
            injectMember {
                method { name = "handlePaymentDetectionMessage" }
                intercept()
            }
        }
    }
}