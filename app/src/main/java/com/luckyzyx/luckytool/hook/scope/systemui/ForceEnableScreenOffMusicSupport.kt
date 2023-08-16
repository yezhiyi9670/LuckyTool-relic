package com.luckyzyx.luckytool.hook.scope.systemui

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.luckyzyx.luckytool.hook.utils.SettingsUtils

object ForceEnableScreenOffMusicSupport : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusBlackScreenGestureControllExImpl
        findClass("com.oplus.systemui.keyguard.OplusBlackScreenGestureControllExImpl").hook {
            injectMember {
                method { name = "resetAodMediaSupportConfig" }
                afterHook {
                    val context = field { name = "mContext" }.get(instance).cast<Context>()
                        ?: return@afterHook
                    SettingsUtils(appClassLoader).Secure.method {
                        name = "putIntForUser";paramCount = 4
                    }.get().call(context.contentResolver, "aod_media_support", 1, 0)
                    "com.oplusos.systemui.notification.util.NotificationStatisticUtil".toClass()
                        .method { name = "setAodMediaSupport";paramCount = 1 }.get().call(true)
                }
            }
        }
    }
}