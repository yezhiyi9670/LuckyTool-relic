package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveTopLockScreenIcon : YukiBaseHooker() {
    override fun onHook() {
        //Source LockIcon
        findClass("com.android.systemui.statusbar.phone.LockIcon").hook {
            injectMember {
                method {
                    name = "updateIconVisibility"
                }
                beforeHook {
                    args(0).setFalse()
                }
            }
        }
    }
}