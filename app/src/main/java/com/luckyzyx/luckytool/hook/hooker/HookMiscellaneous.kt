package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.externalstorage.RemoveStorageLimit
import com.luckyzyx.luckytool.hook.scope.systemui.DisableOTGAutoOff
import com.luckyzyx.luckytool.hook.scope.systemui.ShowChargingRipple
import com.luckyzyx.luckytool.utils.A12
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object HookMiscellaneous : YukiBaseHooker() {
    override fun onHook() {
        if (packageName == "com.android.systemui") {
            //充电纹波
            if (prefs(ModulePrefs).getBoolean("show_charging_ripple", false)) {
                if (SDK >= A12) loadHooker(ShowChargingRipple)
            }
            //禁用OTG自动关闭
            if (prefs(ModulePrefs).getBoolean("disable_otg_auto_off", false)) {
                loadHooker(DisableOTGAutoOff)
            }
        }
//        if (packageName == "com.android.settings") {
            //禁用DPI重启恢复
//            if (prefs(ModulePrefs).getBoolean("disable_dpi_reboot_recovery", false)) {
//                loadHooker(DisableDPIRebootRecovery)
//            }

//            findClass("com.oplus.settings.feature.deviceinfo.DeviceRamInfoItemPreference").hook {
//                injectMember {
//                    method {
//                        name = "onBindViewHolder"
//                    }
//                    beforeHook {
//                        //扩展大小
//                        field {
//                            name = "H"
//                        }.get(instance).set(1024 * 1024 * 100)
//                        //扩展大小图标
//                        field {
//                            name = "I"
//                        }.get(instance).setTrue()
//                        //跳转箭头
//                        field {
//                            name = "J"
//                        }.get(instance).setTrue()
//                    }
//                }
//            }
//        }

        if (packageName == "com.android.externalstorage") {
            //移除存储限制
            if (prefs(ModulePrefs).getBoolean("remove_storage_limit", false)) {
                loadHooker(RemoveStorageLimit)
            }
        }
    }
}