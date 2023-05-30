package com.luckyzyx.luckytool.hook.scope.uiengine

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class SetAodStyleMode(val mode: String) : YukiBaseHooker() {

    override fun onHook() {
        //Source ProductFlavorOption
        findClass("com.oplus.egview.util.ProductFlavorOption").hook {
            injectMember {
                method { name = "isFlavorTwoDevice" }
                when (mode) {
                    "1" -> replaceToTrue()
                    "2" -> replaceToFalse()
                }
            }
        }
    }
}