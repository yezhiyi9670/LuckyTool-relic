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

//        //Source BlackScreenGestureUtil
//        VariousClass(
//            "com.oplusos.systemui.aod.utils.BlackScreenGestureUtil", //C13.1
//            "com.oplus.systemui.keyguard.gesture.BlackScreenGestureUtil" //C14
//        ).hook {
//            injectMember {
//                method { name = "isTouchNodeSupport" }
//                afterHook {
//                    val i = args().first().int()
//                    val i2 = args().last().int()
//                    loggerD(msg = "isTouchNodeSupport -> $i $i2 -> $result")
//                    if (i == 39) resultTrue()
//                }
//            }
//            injectMember {
//                method { name = "readNodeFileByDevice" }
//                afterHook {
//                    val i = args().first().int()
//                    val i2 = args().last().int()
//                    loggerD(msg = "readNodeFileByDevice -> $i $i2 -> $result")
//                    result = "result"
//                }
//            }
//            injectMember {
//                method { name = "hasSupportAodMediaGestureConfig" }
//                afterHook {
//                    loggerD(msg = "hasSupportAodMediaGestureConfig -> $result")
//                    resultTrue()
//                }
//            }
//        }
//
//        findClass("com.oplusos.systemui.aod.AodRecord").hook {
//            injectMember {
//                method { name = "shouldBlockAodShow" }
//                afterHook {
//                    loggerD(msg = "shouldBlockAodShow -> $result")
//                    resultFalse()
//                }
//            }
//        }
    }
}