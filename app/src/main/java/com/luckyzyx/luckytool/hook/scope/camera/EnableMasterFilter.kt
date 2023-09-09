package com.luckyzyx.luckytool.hook.scope.camera

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

object EnableMasterFilter : YukiBaseHooker() {
    override fun onHook() {
        //Source FilterGroupManager
        findClass("com.oplus.camera.filter.FilterGroupManager").hook {
            injectMember {
                method { name = "initVideoFilterGroup" }
                afterHook {
                    val sVideoFilterGroup = field { name = "sVideoFilterGroup" }.get().any()
                        ?: return@afterHook
                    instance.callMasterFilter(sVideoFilterGroup)
                }
            }
            injectMember {
                method { name = "initStickerFilterGroup" }
                afterHook {
                    val sStickerFilterGroup = field { name = "sStickerFilterGroup" }.get().any()
                        ?: return@afterHook
                    instance.callMasterFilter(sStickerFilterGroup)
                }
            }
        }
    }

    private fun Any.callMasterFilter(group: Any) {
        val mbBasicAndStyleNotEmpty = current().field { name = "mbBasicAndStyleNotEmpty" }
            .cast<Boolean>() ?: return
        if (mbBasicAndStyleNotEmpty) return
        current().method {
            name = "initMasterFilterGroup"
            paramCount = 2
        }.call("com.oplus.photo.master.filter.type.list", group)
    }
}