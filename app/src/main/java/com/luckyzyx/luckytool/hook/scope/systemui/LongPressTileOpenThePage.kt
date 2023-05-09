package com.luckyzyx.luckytool.hook.scope.systemui

import android.app.PendingIntent
import android.content.Intent
import android.provider.Settings
import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.android.PendingIntentClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.hook.utils.DependencyUtils
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

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
                beforeHook {
                    if (!isRestore) return@beforeHook
                    val mState = field { name = "mState" }.get(instance).any() ?: return@beforeHook
                    val dualTarget = mState.current().field {
                        name = "dualTarget"
                        superClass()
                    }.boolean()
                    if (dualTarget) {
                        field { name = "mClickHandler" }.get(instance).cast<android.os.Handler>()?.sendEmptyMessage(4)
                        resultNull()
                    }
                }
            }
        }
        //Source OplusQsMediaPanelViewController -> MediaDataUpdateListener
        findClass("com.oplus.systemui.qs.media.OplusQsMediaPanelViewController\$mediaDataUpdateListener\$1").hook {
            injectMember {
                method {
                    name = "onChange"
                    paramCount = 1
                }
                afterHook {
                    val controller = field { order().index().first() }.get(instance).cast<Any>()
                    val mView = controller?.current()?.field {
                        name = "mView"
                        superClass(isOnlySuperClass = true)
                    }?.cast<ViewGroup>()
                    val mediaData = args().first().any()
                    val clickIntent = mediaData?.current()?.method { name = "getClickIntent" }?.invoke<PendingIntent>()
                    mView?.setOnLongClickListener {
                        clickIntent?.let { its -> openIntent(its) }
                        true
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
                    openIntent(Intent(Settings.ACTION_WIFI_SETTINGS), 0)
                }
            }
        }
        //Source OplusCellularTile
        findClass("com.oplusos.systemui.qs.tiles.OplusCellularTile").hook {
            injectMember {
                method {
                    name = "getLongClickIntent"
                }
                beforeHook {
                    if (!isRestore) return@beforeHook
                    val getState = method {
                        name = "getState"
                        superClass()
                    }.get(instance).invoke<Any>()
                    val state = getState?.current()?.field {
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
                    }.get().invoke<Intent>()?.let { openIntent(it, 0) }
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
                        ?.let { openIntent(it, 0) }
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
                    openIntent(Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0)
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
                        ?.let { openIntent(it, 0) }
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
                    }.get(instance).invoke<Intent>()?.let { openIntent(it, 0) }
                }
            }
        }
    }

    private fun openIntent(intent: PendingIntent) {
        val activityStarterCls = "com.android.systemui.plugins.ActivityStarter".toClass()
        val activityStarter = DependencyUtils(appClassLoader).get(activityStarterCls)
        activityStarter?.current()?.method {
            name = "postStartActivityDismissingKeyguard"
            param(PendingIntentClass)
        }?.call(intent)
    }

    @Suppress("SameParameterValue")
    private fun openIntent(intent: Intent, int: Int) {
        val activityStarterCls = "com.android.systemui.plugins.ActivityStarter".toClass()
        val activityStarter = DependencyUtils(appClassLoader).get(activityStarterCls)
        activityStarter?.current()?.method {
            name = "postStartActivityDismissingKeyguard"
            param(IntentClass, IntType)
        }?.call(intent, int)
    }
}