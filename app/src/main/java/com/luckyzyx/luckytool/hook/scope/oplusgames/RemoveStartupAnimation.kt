package com.luckyzyx.luckytool.hook.scope.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object RemoveStartupAnimation : YukiBaseHooker() {
    override fun onHook() {
        //Source GameOptimizedNewView
        //Search -> startAnimationIn -> Method
        findClass("business.secondarypanel.view.GameOptimizedNewView").hook {
            injectMember {
                method { name = "c" }
                intercept()
            }
        }
    }
}
