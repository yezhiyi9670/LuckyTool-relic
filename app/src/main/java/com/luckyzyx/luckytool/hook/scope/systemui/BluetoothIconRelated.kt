package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.ModulePrefs

object BluetoothIconRelated : YukiBaseHooker() {
    override fun onHook() {
        var isHide = prefs(ModulePrefs).getBoolean("hide_icon_when_bluetooth_not_connected", false)
        dataChannel.wait<Boolean>("hide_icon_when_bluetooth_not_connected") { isHide = it }
        //Source PhoneStatusBarPolicyEx
        VariousClass(
            "com.oplusos.systemui.statusbar.phone.PhoneStatusBarPolicyEx", //C13
            "com.oplus.systemui.statusbar.phone.OplusPhoneStatusBarPolicyExImpl" //C14
        ).hook {
            injectMember {
                method { name = "updateBluetoothIcon";paramCount = 4 }
                beforeHook {
                    if (!isHide) return@beforeHook
                    val visibility = args().last().boolean()
                    val mBluetooth = field {
                        name = "mBluetooth"
                        superClass(true)
                    }.get(instance).any() ?: return@beforeHook
                    val isBluetoothEnabled = mBluetooth.current().method {
                        name = "isBluetoothEnabled"
                    }.invoke<Boolean>() ?: return@beforeHook
                    val isBluetoothConnected = mBluetooth.current().method {
                        name = "isBluetoothConnected"
                    }.invoke<Boolean>() ?: return@beforeHook
                    if (visibility && isBluetoothEnabled && isBluetoothConnected) args(3).setTrue()
                    else args().last().setFalse()
                }
            }
        }
    }
}