package com.luckyzyx.luckytool.hook.scope.systemui

import android.app.PendingIntent
import android.content.Intent
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.android.PendingIntentClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.hook.utils.sysui.DependencyUtils
import com.luckyzyx.luckytool.utils.A13
import com.luckyzyx.luckytool.utils.ModulePrefs
import com.luckyzyx.luckytool.utils.SDK

object LongPressTileOpenThePage : YukiBaseHooker() {
    override fun onHook() {
        if (SDK < A13) return
        val isRestore = prefs(ModulePrefs).getBoolean("restore_some_tile_long_press_event", false)

        //QSTileImpl
        findClass("com.android.systemui.qs.tileimpl.QSTileImpl").hook {
            injectMember {
                method { name = "longClick";paramCount = 1 }
                beforeHook {
                    if (!isRestore) return@beforeHook
                    val mState = field { name = "mState" }.get(instance).any() ?: return@beforeHook
                    val dualTarget = mState.current().field {
                        name = "dualTarget";superClass()
                    }.boolean()
                    if (dualTarget) {
                        field { name = "mClickHandler" }.get(instance).cast<android.os.Handler>()
                            ?.sendEmptyMessage(4)
                        resultNull()
                    }
                }
            }
        }
        //Source OplusCellularTile
        VariousClass(
            "com.oplusos.systemui.qs.tiles.OplusCellularTile", //C13
            "com.oplus.systemui.qs.tiles.OplusCellularTile" //C14
        ).hook {
            injectMember {
                method { name = "getLongClickIntent" }
                beforeHook {
                    if (!isRestore) return@beforeHook
                    val getState = method {
                        name = "getState";superClass()
                    }.get(instance).invoke<Any>() ?: return@beforeHook
                    val state = getState.current().field {
                        name = "state";superClass()
                    }.cast<Int>() ?: return@beforeHook
                    result = if (state == 0) Intent("android.settings.WIRELESS_SETTINGS")
                    else method { name = "getCellularSettingIntent";superClass() }.get(instance)
                        .invoke<Intent>()
                }
            }
        }
    }

    @Suppress("SameParameterValue", "unused")
    private fun openIntent(intent: PendingIntent) {
        val activityStarterCls = "com.android.systemui.plugins.ActivityStarter".toClass()
        val activityStarter = DependencyUtils(appClassLoader).get(activityStarterCls)
        activityStarter?.current()?.method {
            name = "postStartActivityDismissingKeyguard"
            param(PendingIntentClass)
        }?.call(intent)
    }

    @Suppress("SameParameterValue", "unused")
    private fun openIntent(intent: Intent, int: Int) {
        val activityStarterCls = "com.android.systemui.plugins.ActivityStarter".toClass()
        val activityStarter = DependencyUtils(appClassLoader).get(activityStarterCls)
        activityStarter?.current()?.method {
            name = "postStartActivityDismissingKeyguard"
            param(IntentClass, IntType)
        }?.call(intent, int)
    }
}