package com.luckyzyx.luckytool.hook.hooker

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.scope.battery.BatteryHiddenEntrance
import com.luckyzyx.luckytool.hook.scope.externalstorage.RemoveStorageLimit
import com.luckyzyx.luckytool.hook.scope.settings.DisableDPIRebootRecovery
import com.luckyzyx.luckytool.hook.scope.systemui.*
import com.luckyzyx.luckytool.utils.data.A12
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.*

class HookMiscellaneous : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //充电纹波
            if (prefs(XposedPrefs).getBoolean("show_charging_ripple",false)) {
                if (SDK >= A12) loadHooker(ShowChargingRipple())
            }
            //禁用OTG自动关闭
            if (prefs(XposedPrefs).getBoolean("disable_otg_auto_off",false)){
                loadHooker(DisableOTGAutoOff())
            }
        }
        loadApp("com.android.settings"){
            //禁用DPI重启恢复
            if (prefs(XposedPrefs).getBoolean("disable_dpi_reboot_recovery",false)) {
                loadHooker(DisableDPIRebootRecovery())
            }
            @Suppress("ConstantConditionIf")
            if (false){
                findClass("com.oplus.settings.feature.deviceinfo.DeviceRamInfoItemPreference").hook {
                    injectMember {
                        method {
                            name = "onBindViewHolder"
                        }
                        beforeHook {
                            //扩展大小
                            field {
                                name = "H"
                            }.get(instance).set(1024*1024*100)
                            //扩展大小图标
                            field {
                                name = "I"
                            }.get(instance).setTrue()
                            //跳转箭头
                            field {
                                name = "J"
                            }.get(instance).setTrue()
                        }
                    }
                }
            }
        }

        loadApp("com.android.externalstorage"){
            //移除存储限制
            if (prefs(XposedPrefs).getBoolean("remove_storage_limit",false)) {
                loadHooker(RemoveStorageLimit())
            }
        }

        loadApp("com.oplus.battery") {
            //屏幕省电,电池健康
            loadHooker(BatteryHiddenEntrance())
        }
    }
}