package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.luckyzyx.luckytool.utils.ModulePrefs

object BluetoothIconRelated : YukiBaseHooker() {
    override fun onHook() {
        var isHide = prefs(ModulePrefs).getBoolean("hide_icon_when_bluetooth_not_connected", false)
        dataChannel.wait<Boolean>("hide_icon_when_bluetooth_not_connected") { isHide = it }
        //Source PhoneStatusBarPolicyEx
        findClass("com.oplusos.systemui.statusbar.phone.PhoneStatusBarPolicyEx").hook {
            injectMember {
                method {
                    name = "updateBluetoothIcon"
                }
                beforeHook {
                    if (!isHide) return@beforeHook
                    val visibility = args(3).boolean()
                    val mBluetooth = field {
                        name = "mBluetooth"
                        superClass(isOnlySuperClass = true)
                    }.get(instance).any() ?: return@beforeHook
                    val isBluetoothEnabled = mBluetooth.current().method {
                        name = "isBluetoothEnabled"
                    }.invoke<Boolean>()!!
                    val isBluetoothConnected = mBluetooth.current().method {
                        name = "isBluetoothConnected"
                    }.invoke<Boolean>()!!
                    if (visibility && isBluetoothEnabled && isBluetoothConnected) args(3).setTrue()
                    else args(3).setFalse()
                }
            }
        }
    }
}