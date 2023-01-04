package com.luckyzyx.luckytool.hook.scope.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveHighPerformanceModeNotifications : YukiBaseHooker() {
    override fun onHook() {
        //Source GTModeBroadcastReceiver
        //Search getIntForUser gt_mode_block_message_setting
        findClass("com.oplus.performance.GTModeBroadcastReceiver").hook {
            injectMember {
                method {
                    paramCount = 1
                    returnType(BooleanType).index().first()
                }
                replaceToTrue()
            }.onNoSuchMemberFailure {
                loggerD(msg = "$packageName\nError -> RemoveHighPerformanceModeNotifications")
            }
        }
    }
}