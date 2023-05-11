package com.luckyzyx.luckytool.hook.scope.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.luckyzyx.luckytool.utils.ModulePrefs

object RemoveLockScreenRedOne : YukiBaseHooker() {
    override fun onHook() {
        var isRemove = prefs(ModulePrefs).getBoolean("remove_lock_screen_redone", false)
        dataChannel.wait<Boolean>("remove_lock_screen_redone") { isRemove = it }

        //Source RedTextClock
        findClass("com.oplusos.systemui.keyguard.clock.RedTextClock").hook {
            injectMember {
                method { name = "onTimeChanged" }
                beforeHook {
                    if (isRemove) field {
                        name = "NUMBER_ONE"
                        type = StringClass
                    }.get().set("")
                }
            }
        }
    }
}