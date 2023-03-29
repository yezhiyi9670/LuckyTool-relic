package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Intent
import android.provider.Settings
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.data.A13
import com.luckyzyx.luckytool.utils.data.SDK
import com.luckyzyx.luckytool.utils.tools.ModulePrefs

object LongPressTileOpenThePage : YukiBaseHooker() {
    override fun onHook() {
        if (SDK < A13) return
        val isRestore = prefs(ModulePrefs).getBoolean("restore_some_tile_long_press_event", false)
        val isWifi =
            false//prefs(ModulePrefs).getBoolean("long_press_wifi_tile_open_the_page", false)
        val isMobile =
            false//prefs(ModulePrefs).getBoolean("long_press_mobile_data_tile_open_the_page", false)
        val isWifiAp =
            false//prefs(ModulePrefs).getBoolean("long_press_wifi_hotspot_tile_open_the_page", false)
        val isBluetooth =
            false//prefs(ModulePrefs).getBoolean("long_press_bluetooth_tile_open_the_page", false)
        val isScreenshot =
            false//prefs(ModulePrefs).getBoolean("long_press_screenshot_tile_open_the_page", false)
        val isDnd = false//prefs(ModulePrefs).getBoolean("long_press_dnd_tile_open_the_page", false)
        //QSTileImpl
        findClass("com.android.systemui.qs.tileimpl.QSTileImpl").hook {
            injectMember {
                method {
                    name = "longClick"
                    paramCount = 1
                }
                if (isRestore) beforeHook {
                    val mState = field {
                        name = "mState"
                    }.get(instance).any() ?: return@beforeHook
                    val dualTarget = mState.current().field {
                        name = "dualTarget"
                        superClass()
                    }.boolean()
                    if (dualTarget) {
                        field {
                            name = "mClickHandler"
                        }.get(instance).cast<android.os.Handler>()?.sendEmptyMessage(4)
                        resultNull()
                    }
                }
            }
        }
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
                    name = "getLongClickIntent"
                }
                if (isRestore) beforeHook {
                    val state = method {
                        name = "getState"
                        superClass()
                    }.get(instance).invoke<Any>()?.current()?.field {
                        name = "state"
                        superClass()
                    }?.int()
                    result = if (state == 0) Intent("android.settings.WIRELESS_SETTINGS")
                    else method {
                        name = "getCellularSettingIntent"
                        superClass()
                    }.get(instance).invoke<Intent>()
                }
            }
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
                    method { name = "getLongClickIntent" }.get(instance).invoke<Intent>()
                        ?.let { instance.openIntent(it, 0) }
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
        //Source ScreenshotTile
        findClass("com.oplusos.systemui.qs.tiles.ScreenshotTile").hook {
            injectMember {
                method {
                    name = "handleSecondaryClick"
                }
                if (isScreenshot) replaceUnit {
                    method { name = "getLongClickIntent" }.get(instance).invoke<Intent>()
                        ?.let { instance.openIntent(it, 0) }
                }
            }
        }
        //Source OplusDndTile
        findClass("com.oplusos.systemui.qs.tiles.OplusDndTile").hook {
            injectMember {
                method {
                    name = "handleSecondaryClick"
                }
                if (isDnd) replaceUnit {
                    method {
                        name = "getLongClickIntent"
                        superClass(isOnlySuperClass = true)
                    }.get(instance).invoke<Intent>()?.let { instance.openIntent(it, 0) }
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