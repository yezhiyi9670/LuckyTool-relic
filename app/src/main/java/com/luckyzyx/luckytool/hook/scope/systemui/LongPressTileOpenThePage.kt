package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Intent
import android.provider.Settings
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object LongPressTileOpenThePage : YukiBaseHooker() {
    override fun onHook() {
        val isWifi = prefs(ModulePrefs).getBoolean("long_press_wifi_tile_open_the_page", false)
        val isMobile =
            prefs(ModulePrefs).getBoolean("long_press_mobile_data_tile_open_the_page", false)
        val isWifiAp =
            prefs(ModulePrefs).getBoolean("long_press_wifi_hotspot_tile_open_the_page", false)
        val isBluetooth =
            prefs(ModulePrefs).getBoolean("long_press_bluetooth_tile_open_the_page", false)
        //Source OplusWifiTile
        findClass("com.oplusos.systemui.qs.tiles.OplusWifiTile").hook {
            injectMember {
                method {
                    name = "handleSecondaryClick"
                }
                if (isWifi) replaceUnit {
                    instance.openIntent(Intent(Settings.ACTION_WIFI_SETTINGS), 0)
                }
            }
        }
        //Source OplusCellularTile
        findClass("com.oplusos.systemui.qs.tiles.OplusCellularTile").hook {
            injectMember {
                method {
                    name = "handleSecondaryClick"
                }
                if (isMobile) replaceUnit {
                    method {
                        name = "getCellularSettingIntent"
                    }.get().invoke<Intent>()?.let { instance.openIntent(it, 0) }
                }
            }
        }
        //Source OplusHotspotTile
        findClass("com.oplusos.systemui.qs.tiles.OplusHotspotTile").hook {
            injectMember {
                method {
                    name = "handleSecondaryClick"
                }
                if (isWifiAp) replaceUnit {
                    instance.openIntent(Intent("android.settings.OPLUS_WIFI_AP_SETTINGS"), 0)
                }
            }
        }
        //Source OplusBluetoothTile
        findClass("com.oplusos.systemui.qs.tiles.OplusBluetoothTile").hook {
            injectMember {
                method {
                    name = "handleSecondaryClick"
                }
                if (isBluetooth) replaceUnit {
                    instance.openIntent(Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0)
                }
            }
        }
    }

    private fun Any.openIntent(intent: Intent, int: Int) {
        current().field {
            name = "mActivityStarter"
            superClass()
        }.any()?.current()?.method {
            name = "postStartActivityDismissingKeyguard"
            param(IntentClass, IntType)
            paramCount = 2
        }?.call(intent, int)
    }
}